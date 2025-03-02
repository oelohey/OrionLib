package dev.oelohey.orion.infrastructure.wrapper_world.tick_scheduler;

import dev.oelohey.orion.infrastructure.wrapper_world.chunk.SpatialChunkPos;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.QueryableTickScheduler;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.LongPredicate;

public class WrarppedTickScheduler<T> implements QueryableTickScheduler<T> {

    private static final Comparator<SpatialChunkTickScheduler<?>> COMPARATOR = (a, b) -> OrderedTick.BASIC_COMPARATOR.compare(a.peekNextTick(), b.peekNextTick());

    private final HashMap<SpatialChunkPos, SpatialChunkTickScheduler<T>> chunkTickSchedulers = new HashMap<>();

    private final HashMap<SpatialChunkPos, Long> nextTriggerTickByChunkPos = Util.make(new Long2LongOpenHashMap(), map -> map.defaultReturnValue(Long.MAX_VALUE));

    private final Queue<ChunkTickScheduler<T>> tickableChunkTickSchedulers = new PriorityQueue(COMPARATOR);

    private final Queue<OrderedTick<T>> tickableTicks = new ArrayDeque<>();
    private final List<OrderedTick<T>> tickedTicks = new ArrayList<>();

    private final Set<OrderedTick<?>> copiedTickableTicksList = new ObjectOpenCustomHashSet<>(OrderedTick.HASH_STRATEGY);

    private final BiConsumer<SpatialChunkTickScheduler<T>, OrderedTick<T>> queuedTickConsumer = (chunkTickScheduler, tick) -> {
        if (tick.equals(chunkTickScheduler.peekNextTick())) {
            this.schedule(tick);
        }
    };

    private void schedule(OrderedTick<T> tick) {

    }

    public void addSpatialChunkTickScheduler(SpatialChunkPos pos, SpatialChunkTickScheduler<T> scheduler) {
        this.chunkTickSchedulers.put(pos, scheduler);
        OrderedTick<T> orderedTick = scheduler.peekNextTick();
        if (orderedTick != null) this.nextTriggerTickByChunkPos.put(pos, orderedTick.triggerTick());

        scheduler.setTickConsumer(this.queuedTickConsumer);
    }

    public void removeChunkTickScheduler(SpatialChunkPos pos) {
        SpatialChunkTickScheduler<T> spatialChunkTickScheduler = this.chunkTickSchedulers.remove(pos);
        this.nextTriggerTickByChunkPos.remove(pos);
        if (spatialChunkTickScheduler != null) spatialChunkTickScheduler.setTickConsumer(null);

    }

    @Override
    public void scheduleTick(OrderedTick orderedTick) {

    }

    public void tick(long time, int maxTicks, BiConsumer<BlockPos, T> ticker) {

    }

    @Override
    public boolean isTicking(BlockPos pos, Object type) {
        return false;
    }

    @Override
    public boolean isQueued(BlockPos pos, Object type) {
        return false;
    }

    @Override
    public int getTickCount() {
        return 0;
    }
}
