package dev.oelohey.orion.infrastructure.wrapper_world.tick_scheduler;

import dev.oelohey.orion.OrionLib;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.tick.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ChunkspaceTickScheduler<T> implements SerializableTickScheduler<T>, BasicTickScheduler<T> {

    private final Queue<OrderedTick<T>> orderedTickQueue = new PriorityQueue<>(OrderedTick.TRIGGER_TICK_COMPARATOR);

    @Nullable
    private List<Tick<T>> ticks;

    private final Set<OrderedTick<?>> queuedTicks = new ObjectOpenCustomHashSet<>(OrderedTick.HASH_STRATEGY);

    @Nullable
    private BiConsumer<ChunkspaceTickScheduler<T>, OrderedTick<T>> tickConsumer;

    public ChunkspaceTickScheduler(){}

    public ChunkspaceTickScheduler(List<Tick<T>> ticks) {
        this.ticks = ticks;

        for (Tick<T> tick : ticks) {
            this.queuedTicks.add(OrderedTick.create(tick.type(), tick.pos()));
        }
    }

    public void setTickConsumer(@Nullable BiConsumer<ChunkspaceTickScheduler<T>, OrderedTick<T>> tickConsumer) {
        this.tickConsumer = tickConsumer;
    }

    @Nullable
    public OrderedTick<T> peekNextTick() {
        return this.orderedTickQueue.peek();
    }

    @Nullable
    public OrderedTick<T> pollNextTick() {
        OrderedTick<T> orderedTick = this.orderedTickQueue.poll();
        if (orderedTick != null) this.queuedTicks.remove(orderedTick);
        return orderedTick;
    }

    @Override
    public void scheduleTick(OrderedTick<T> orderedTick) {
        if (this.queuedTicks.add(orderedTick)) {
            this.queueTick(orderedTick);
        }
    }

    private void queueTick(OrderedTick<T> orderedTick) {
        this.orderedTickQueue.add(orderedTick);
        if (this.tickConsumer != null) {
            this.tickConsumer.accept(this, orderedTick);
        }
    }

    @Override
    public boolean isQueued(BlockPos pos, T type) {
        return this.queuedTicks.contains(OrderedTick.create(type, pos));
    }

    public void removeTicksIf(Predicate<OrderedTick<T>> predicate) {
        Iterator<OrderedTick<T>> iterator = this.orderedTickQueue.iterator();

        while (iterator.hasNext()) {
            OrderedTick<T> orderedTick = iterator.next();
            if (predicate.test(orderedTick)) {
                iterator.remove();
                this.queuedTicks.remove(orderedTick);
            }
        }
    }

    public Stream<OrderedTick<T>> getQueueAsStream() {
        return this.orderedTickQueue.stream();
    }

    @Override
    public int getTickCount() {
        return this.orderedTickQueue.size() + (this.ticks != null ? this.ticks.size() : 0);
    }

    @Override
    public List<Tick<T>> collectTicks(long time) {
        List<Tick<T>> list = new ArrayList<>(this.orderedTickQueue.size());
        if (this.ticks != null) list.addAll(this.ticks);

        for (OrderedTick<T> orderedTick : this.orderedTickQueue) list.add(orderedTick.toTick(time));

        return list;
    }

    public void disable(long time) {
        if (this.ticks != null) {
            int i = -this.ticks.size();

            for (Tick<T> tick : this.ticks) {
                this.queueTick(tick.createOrderedTick(time, i++));
            }
        }

        this.ticks = null;
    }

    public static <T> ChunkTickScheduler<T> create(NbtList tickQueue, Function<String, Optional<T>> nameToTypeFunction, ChunkPos pos) {
        return new ChunkTickScheduler<>(Tick.tick(tickQueue, nameToTypeFunction, pos));
    }
}
