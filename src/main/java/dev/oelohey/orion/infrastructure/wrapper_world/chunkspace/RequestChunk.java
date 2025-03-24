package dev.oelohey.orion.infrastructure.wrapper_world.chunkspace;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.gen.chunk.BlendingData;
import net.minecraft.world.tick.BasicTickScheduler;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class RequestChunk extends Chunk {

    //TO DO

    public RequestChunk(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable ChunkSection[] sectionArray, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biomeRegistry, inhabitedTime, sectionArray, blendingData);
    }

    @Override
    public @Nullable BlockState setBlockState(BlockPos pos, BlockState state, boolean moved) {
        return null;
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity) {

    }

    @Override
    public void addEntity(Entity entity) {

    }

    @Override
    public ChunkStatus getStatus() {
        return null;
    }

    @Override
    public void removeBlockEntity(BlockPos pos) {

    }

    @Override
    public @Nullable NbtCompound getPackedBlockEntityNbt(BlockPos pos, RegistryWrapper.WrapperLookup registries) {
        return null;
    }

    @Override
    public BasicTickScheduler<Block> getBlockTickScheduler() {
        return null;
    }

    @Override
    public BasicTickScheduler<Fluid> getFluidTickScheduler() {
        return null;
    }

    @Override
    public TickSchedulers getTickSchedulers(long time) {
        return null;
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return null;
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return super.getBlockEntity(pos, type);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return null;
    }

    @Override
    public int getLuminance(BlockPos pos) {
        return super.getLuminance(pos);
    }

    @Override
    public Stream<BlockState> getStatesInBox(Box box) {
        return super.getStatesInBox(box);
    }

    @Override
    public BlockHitResult raycast(BlockStateRaycastContext context) {
        return super.raycast(context);
    }

    @Override
    public BlockHitResult raycast(RaycastContext context) {
        return super.raycast(context);
    }

    @Override
    public @Nullable BlockHitResult raycastBlock(Vec3d start, Vec3d end, BlockPos pos, VoxelShape shape, BlockState state) {
        return super.raycastBlock(start, end, pos, shape, state);
    }

    @Override
    public double getDismountHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
        return super.getDismountHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
    }

    @Override
    public double getDismountHeight(BlockPos pos) {
        return super.getDismountHeight(pos);
    }

    @Override
    public <A> @Nullable A getAttached(AttachmentType<A> type) {
        return super.getAttached(type);
    }

    @Override
    public <A> A getAttachedOrThrow(AttachmentType<A> type) {
        return super.getAttachedOrThrow(type);
    }

    @Override
    public <A> A getAttachedOrSet(AttachmentType<A> type, A defaultValue) {
        return super.getAttachedOrSet(type, defaultValue);
    }

    @Override
    public <A> A getAttachedOrCreate(AttachmentType<A> type, Supplier<A> initializer) {
        return super.getAttachedOrCreate(type, initializer);
    }

    @Override
    public <A> A getAttachedOrCreate(AttachmentType<A> type) {
        return super.getAttachedOrCreate(type);
    }

    @Override
    public <A> A getAttachedOrElse(AttachmentType<A> type, @Nullable A defaultValue) {
        return super.getAttachedOrElse(type, defaultValue);
    }

    @Override
    public <A> A getAttachedOrGet(AttachmentType<A> type, Supplier<A> defaultValue) {
        return super.getAttachedOrGet(type, defaultValue);
    }

    @Override
    public <A> @Nullable A setAttached(AttachmentType<A> type, @Nullable A value) {
        return super.setAttached(type, value);
    }

    @Override
    public boolean hasAttached(AttachmentType<?> type) {
        return super.hasAttached(type);
    }

    @Override
    public <A> @Nullable A removeAttached(AttachmentType<A> type) {
        return super.removeAttached(type);
    }

    @Override
    public <A> @Nullable A modifyAttached(AttachmentType<A> type, UnaryOperator<A> modifier) {
        return super.modifyAttached(type, modifier);
    }

    @Override
    public @Nullable Object getBlockEntityRenderData(BlockPos pos) {
        return super.getBlockEntityRenderData(pos);
    }

    @Override
    public boolean hasBiomes() {
        return super.hasBiomes();
    }

    @Override
    public @UnknownNullability RegistryEntry<Biome> getBiomeFabric(BlockPos pos) {
        return super.getBiomeFabric(pos);
    }

    @Override
    public int getTopYInclusive() {
        return super.getTopYInclusive();
    }

    @Override
    public int countVerticalSections() {
        return super.countVerticalSections();
    }

    @Override
    public int getBottomSectionCoord() {
        return super.getBottomSectionCoord();
    }

    @Override
    public int getTopSectionCoord() {
        return super.getTopSectionCoord();
    }

    @Override
    public boolean isInHeightLimit(int y) {
        return super.isInHeightLimit(y);
    }

    @Override
    public boolean isOutOfHeightLimit(BlockPos pos) {
        return super.isOutOfHeightLimit(pos);
    }

    @Override
    public boolean isOutOfHeightLimit(int y) {
        return super.isOutOfHeightLimit(y);
    }

    @Override
    public int getSectionIndex(int y) {
        return super.getSectionIndex(y);
    }

    @Override
    public int sectionCoordToIndex(int coord) {
        return super.sectionCoordToIndex(coord);
    }

    @Override
    public int sectionIndexToCoord(int index) {
        return super.sectionIndexToCoord(index);
    }
}
