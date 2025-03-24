package dev.oelohey.orion.infrastructure.wrapper_world.tick_scheduler;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.infrastructure.wrapper_world.chunkspace.ChunkspacePosition;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.QueryableTickScheduler;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class WrapperTickScheduler<T> implements QueryableTickScheduler<T> {

    private static final Comparator<ChunkspaceTickScheduler<?>> COMPARATOR = (a, b) -> OrderedTick.BASIC_COMPARATOR.compare(a.peekNextTick(), b.peekNextTick());

    private final HashMap<ChunkspacePosition, ChunkspaceTickScheduler<T>> chunkTickSchedulers = new HashMap<>();

    private final HashMap<ChunkspacePosition, Long> nextTriggerTickByChunkPos = new HashMap<>();

    private final Queue<ChunkspaceTickScheduler<T>> tickableChunkTickSchedulers = new PriorityQueue<>(COMPARATOR);

    private final Queue<OrderedTick<T>> tickableTicks = new ArrayDeque<>();
    private final List<OrderedTick<T>> tickedTicks = new ArrayList<>();

    private final Set<OrderedTick<?>> copiedTickableTicksList = new ObjectOpenCustomHashSet<>(OrderedTick.HASH_STRATEGY);

    private final BiConsumer<ChunkspaceTickScheduler<T>, OrderedTick<T>> queuedTickConsumer = (chunkTickScheduler, tick) -> {
        if (tick.equals(chunkTickScheduler.peekNextTick())) {
            this.schedule(tick);
        }
    };

    public void addChunkspaceTickScheduler(ChunkspacePosition pos, ChunkspaceTickScheduler<T> scheduler) {
        this.chunkTickSchedulers.put(pos, scheduler);
        OrderedTick<T> orderedTick = scheduler.peekNextTick();
        if (orderedTick != null) this.nextTriggerTickByChunkPos.put(pos, orderedTick.triggerTick());

        scheduler.setTickConsumer(this.queuedTickConsumer);
    }

    public void removeChunkTickScheduler(ChunkspacePosition pos) {
        ChunkspaceTickScheduler<T> spatialChunkTickScheduler = this.chunkTickSchedulers.remove(pos);
        this.nextTriggerTickByChunkPos.remove(pos);
        if (spatialChunkTickScheduler != null) spatialChunkTickScheduler.setTickConsumer(null);

    }

    @Override
    public void scheduleTick(OrderedTick<T> orderedTick) {
        OrionLib.LOGGER.info("x: {}, y: {}, z: {}", orderedTick.pos().getX(), orderedTick.pos().getY(), orderedTick.pos().getZ());
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(orderedTick.pos());
        OrionLib.LOGGER.info("x: {}, y: {}, z: {}", spatialChunkPos.getX(), spatialChunkPos.getY(), spatialChunkPos.getZ());
        ChunkspaceTickScheduler<T> chunkTickScheduler = this.chunkTickSchedulers.get(spatialChunkPos);
        if (chunkTickScheduler == null) {
            chunkTickScheduler = new ChunkspaceTickScheduler<>();
            addChunkspaceTickScheduler(spatialChunkPos, chunkTickScheduler);
            OrionLib.LOGGER.info("a");
        }
        OrionLib.LOGGER.info("b");
        chunkTickScheduler.scheduleTick(orderedTick);
    }

    public void tick(long time, int maxTicks, BiConsumer<BlockPos, T> ticker) {
        Profiler profiler = Profilers.get();
        profiler.push("collect");
        this.collectTickableTicks(time, maxTicks, profiler);
        profiler.swap("run");
        profiler.visit("ticksToRun", this.tickableTicks.size());
        this.tick(ticker);
        profiler.swap("cleanup");
        this.clear();
        profiler.pop();
    }

    private void collectTickableTicks(long time, int maxTicks, Profiler profiler) {
        this.collectTickableChunkTickSchedulers(time);
        profiler.visit("containersToTick", this.tickableChunkTickSchedulers.size());
        this.addTickableTicks(time, maxTicks);
        this.delayAllTicks();
    }

    private void collectTickableChunkTickSchedulers(long time) {
        Set<Map.Entry<ChunkspacePosition, Long>> objectIterator = this.nextTriggerTickByChunkPos.entrySet();

        for (Map.Entry<ChunkspacePosition, Long> entry : objectIterator){
            ChunkspacePosition pos = entry.getKey();
            long m = entry.getValue();
            if (m <= time){
                ChunkspaceTickScheduler<T> spatialChunkTickScheduler = this.chunkTickSchedulers.get(pos);
                if (spatialChunkTickScheduler != null){
                    OrderedTick<T> orderedTick = spatialChunkTickScheduler.peekNextTick();
                    if (orderedTick != null){
                        if (orderedTick.triggerTick() > time) entry.setValue(orderedTick.triggerTick());
                        this.tickableChunkTickSchedulers.add(spatialChunkTickScheduler);
                    }
                }
            }
        }
    }

    private void addTickableTicks(long time, int maxTicks) {
        ChunkspaceTickScheduler<T> chunkTickScheduler;

        while (this.isTickableTicksCountUnder(maxTicks) && (chunkTickScheduler = this.tickableChunkTickSchedulers.poll()) != null) {
            OrderedTick<T> orderedTick = chunkTickScheduler.pollNextTick();

            this.addTickableTick(orderedTick);

            this.addTickableTicks(this.tickableChunkTickSchedulers, chunkTickScheduler, time, maxTicks);

            OrderedTick<T> orderedTick2 = chunkTickScheduler.peekNextTick();
            if (orderedTick2 != null) {
                if (orderedTick2.triggerTick() <= time && this.isTickableTicksCountUnder(maxTicks)) {
                    this.tickableChunkTickSchedulers.add(chunkTickScheduler);
                } else {
                    this.schedule(orderedTick2);
                }
            }
        }
    }

    private void delayAllTicks() {
        for (ChunkspaceTickScheduler<T> chunkTickScheduler : this.tickableChunkTickSchedulers) {
            this.schedule(Objects.requireNonNull(chunkTickScheduler.peekNextTick()));
        }
    }

    private void schedule(OrderedTick<T> tick) {
        this.nextTriggerTickByChunkPos.put(ChunkspacePosition.chunkPosFromBlockPos(tick.pos()), tick.triggerTick());
    }

    private void addTickableTicks(Queue<ChunkspaceTickScheduler<T>> tickableChunkTickSchedulers, ChunkspaceTickScheduler<T> chunkTickScheduler, long tick, int maxTicks) {
        if (this.isTickableTicksCountUnder(maxTicks)) {
            ChunkspaceTickScheduler<T> chunkTickScheduler2 = tickableChunkTickSchedulers.peek();
            OrderedTick<T> orderedTick = chunkTickScheduler2 != null ? chunkTickScheduler2.peekNextTick() : null;

            while (this.isTickableTicksCountUnder(maxTicks)) {
                OrderedTick<T> orderedTick2 = chunkTickScheduler.peekNextTick();
                if (orderedTick2 == null || orderedTick2.triggerTick() > tick || orderedTick != null && OrderedTick.BASIC_COMPARATOR.compare(orderedTick2, orderedTick) > 0
                )
                {
                    break;
                }

                chunkTickScheduler.pollNextTick();
                this.addTickableTick(orderedTick2);
            }
        }
    }

    private void addTickableTick(OrderedTick<T> tick) {
        this.tickableTicks.add(tick);
    }

    private boolean isTickableTicksCountUnder(int maxTicks) {
        return this.tickableTicks.size() < maxTicks;
    }

    private void tick(BiConsumer<BlockPos, T> ticker) {
        if (!this.tickableTicks.isEmpty()) {
            OrionLib.LOGGER.info(this.tickableTicks + "abc");
        }
        while (!this.tickableTicks.isEmpty()) {
            OrderedTick<T> orderedTick = this.tickableTicks.poll();
            OrionLib.LOGGER.info(orderedTick.toString()+"a");
            if (!this.copiedTickableTicksList.isEmpty()) {
                this.copiedTickableTicksList.remove(orderedTick);
            }

            this.tickedTicks.add(orderedTick);
            ticker.accept(orderedTick.pos(), orderedTick.type());
        }
    }

    private void clear() {
        this.tickableTicks.clear();
        this.tickableChunkTickSchedulers.clear();
        this.tickedTicks.clear();
        this.copiedTickableTicksList.clear();
    }

    @Override
    public boolean isQueued(BlockPos pos, T type) {
        ChunkspaceTickScheduler<T> chunkTickScheduler = this.chunkTickSchedulers.get(ChunkspacePosition.chunkPosFromBlockPos(pos));
        return chunkTickScheduler != null && chunkTickScheduler.isQueued(pos, type);
    }

    @Override
    public boolean isTicking(BlockPos pos, T type) {
        this.copyTickableTicksList();
        return this.copiedTickableTicksList.contains(OrderedTick.create(type, pos));
    }

    private void copyTickableTicksList() {
        if (this.copiedTickableTicksList.isEmpty() && !this.tickableTicks.isEmpty()) {
            this.copiedTickableTicksList.addAll(this.tickableTicks);
        }
    }

    private void visitChunks(BlockBox box, WrapperTickScheduler.ChunkVisitor<T> visitor) {
        int i = ChunkSectionPos.getSectionCoord((double)box.getMinX());
        int j = ChunkSectionPos.getSectionCoord((double)box.getMinZ());
        int k = ChunkSectionPos.getSectionCoord((double)box.getMaxX());
        int l = ChunkSectionPos.getSectionCoord((double)box.getMaxZ());

        for (int m = i; m <= k; m++) {
            for (int n = j; n <= l; n++) {
                long o = ChunkPos.toLong(m, n);
                ChunkspaceTickScheduler<T> chunkTickScheduler = this.chunkTickSchedulers.get(o);
                if (chunkTickScheduler != null) {
                    visitor.accept(o, chunkTickScheduler);
                }
            }
        }
    }

    public void scheduleTicks(BlockBox box, Vec3i offset) {
        this.scheduleTicks(this, box, offset);
    }

    public void scheduleTicks(WrapperTickScheduler<T> scheduler, BlockBox box, Vec3i offset) {
        List<OrderedTick<T>> list = new ArrayList<>();
        Predicate<OrderedTick<T>> predicate = tick -> box.contains(tick.pos());
        scheduler.tickedTicks.stream().filter(predicate).forEach(list::add);
        scheduler.tickableTicks.stream().filter(predicate).forEach(list::add);
        scheduler.visitChunks(box, (chunkPos, chunkTickScheduler) -> chunkTickScheduler.getQueueAsStream().filter(predicate).forEach(list::add));
        LongSummaryStatistics longSummaryStatistics = list.stream().mapToLong(OrderedTick::subTickOrder).summaryStatistics();
        long l = longSummaryStatistics.getMin();
        long m = longSummaryStatistics.getMax();
        list.forEach(
                tick -> this.scheduleTick(new OrderedTick<>((T)tick.type(), tick.pos().add(offset), tick.triggerTick(), tick.priority(), tick.subTickOrder() - l + m + 1L))
        );
    }

    @Override
    public int getTickCount() {
        return 0;
    }

    @FunctionalInterface
    interface ChunkVisitor<T> {
        void accept(long chunkPos, ChunkspaceTickScheduler<T> chunkTickScheduler);
    }
}
