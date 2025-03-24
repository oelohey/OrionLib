package dev.oelohey.orion.infrastructure.wrapper_world;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.entity.WrapperEntity;
import dev.oelohey.orion.infrastructure.wrapper_world.chunkspace.Chunkspace;
import dev.oelohey.orion.infrastructure.wrapper_world.chunkspace.ChunkspaceManager;
import dev.oelohey.orion.infrastructure.wrapper_world.chunkspace.ChunkspacePosition;
import dev.oelohey.orion.infrastructure.wrapper_world.tick_scheduler.WrapperTickScheduler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.tick.QueryableTickScheduler;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Wrapper extends World {

    public final WrapperEntity ENTITY;
    private final ChunkspaceManager chunkManager = new ChunkspaceManager();

    public Wrapper(WrapperEntity entity) {
        super(new WrapperWorldProperties(entity), entity.getWorld().getRegistryKey(), entity.getRegistryManager(), entity.getWorld().getDimensionEntry(), false, false, 0, 512);
        ENTITY = entity;
    }

    public void tick(){
        long l = this.ENTITY.getWorld().getTime();

        chunkManager.tick();
        this.blockTickScheduler.tick(l, 65536, this::tickBlock);
        this.fluidTickScheduler.tick(l, 65536, this::tickFluid);
    }

    private void tickFluid(BlockPos pos, Fluid fluid) {
        BlockState blockState = this.getBlockState(pos);
        FluidState fluidState = blockState.getFluidState();
        if (fluidState.isOf(fluid)) {
            OrionLib.INTERFACE.TARGET = this;
            fluidState.onScheduledTick(OrionLib.INTERFACE, pos, blockState);
        }
    }

    private void tickBlock(BlockPos pos, Block block) {
        BlockState blockState = this.getBlockState(pos);
        if (blockState.isOf(block)) {
            OrionLib.INTERFACE.TARGET = this;
            blockState.scheduledTick(OrionLib.INTERFACE, pos, this.random);
        }
    }

    private final WrapperTickScheduler<Block> blockTickScheduler = new WrapperTickScheduler<>();
    private final WrapperTickScheduler<Fluid> fluidTickScheduler = new WrapperTickScheduler<>();

    //Block Logic
    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);

        if (!validateChunkPos(spatialChunkPos)){
            return false;
        } else if(!this.chunkManager.doesChunkExist(spatialChunkPos)){
            Chunkspace wrapperChunk = new Chunkspace(this, spatialChunkPos);
            this.chunkManager.setChunk(wrapperChunk);
        }

        Chunkspace wrapperChunk = this.chunkManager.getChunk(spatialChunkPos);
        if (wrapperChunk != null) {
            BlockState stateOld = wrapperChunk.getBlockState(pos);
            if (stateOld == null) stateOld = Blocks.AIR.getDefaultState();
            BlockState blockState = wrapperChunk.setBlockState(pos, state, (flags & Block.MOVED) != 0);
            this.onBlockChanged(pos,stateOld, blockState);
            return blockState != null;
        }
        return false;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);
        if (!validateChunkPos(spatialChunkPos)) {
            return Blocks.VOID_AIR.getDefaultState();
        }else if (!this.chunkManager.doesChunkExist(spatialChunkPos)){
            return Blocks.AIR.getDefaultState();
        }
        else{
            return Objects.requireNonNull(this.chunkManager.getChunk(spatialChunkPos)).getBlockState(pos);
        }
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);
        if (!validateChunkPos(spatialChunkPos)) {
            return Fluids.EMPTY.getDefaultState();
        }else if (!this.chunkManager.doesChunkExist(spatialChunkPos)){
            return Fluids.EMPTY.getDefaultState();
        }
        else{
            return Objects.requireNonNull(this.chunkManager.getChunk(spatialChunkPos)).getFluidState(pos);
        }
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);
        if (!validateChunkPos(spatialChunkPos)) {
            return null;
        }else if (this.chunkManager.doesChunkExist(spatialChunkPos)){
            return null;
        }
        else{
            return Objects.requireNonNull(this.chunkManager.getChunk(spatialChunkPos)).getBlockEntity(pos);
        }
    }

    @Override
    public void addBlockEntity(BlockEntity blockEntity) {
        BlockPos pos = blockEntity.getPos();
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);
        if (validateChunkPos(spatialChunkPos) && this.chunkManager.doesChunkExist(spatialChunkPos)) {
            Objects.requireNonNull(this.chunkManager.getChunk(spatialChunkPos)).setBlockEntity(blockEntity);
        }
    }

    @Override
    public void removeBlockEntity(BlockPos pos) {
        ChunkspacePosition spatialChunkPos = ChunkspacePosition.chunkPosFromBlockPos(pos);
        if (validateChunkPos(spatialChunkPos) && this.chunkManager.doesChunkExist(spatialChunkPos)) {
            Objects.requireNonNull(this.chunkManager.getChunk(spatialChunkPos)).removeBlockEntity(pos);
        }
    }

    @Override
    public boolean breakBlock(BlockPos pos, boolean drop, @Nullable Entity breakingEntity, int maxUpdateDepth) {
        BlockState blockState = this.getBlockState(pos);
        if (blockState.isAir()) {
            return false;
        } else {
            FluidState fluidState = this.getFluidState(pos);
            if (!(blockState.getBlock() instanceof AbstractFireBlock)) {
                this.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockState));
            }

            if (drop) {
                BlockEntity blockEntity = blockState.hasBlockEntity() ? this.getBlockEntity(pos) : null;
                Block.dropStacks(blockState, this.ENTITY.getWorld(), pos, blockEntity, breakingEntity, ItemStack.EMPTY);
            }

            boolean bl = this.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL, maxUpdateDepth);
            if (bl) {
                this.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(breakingEntity, blockState));
            }

            return bl;
        }
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return this.ENTITY.getServer();
    }

    private boolean validateChunkPos(ChunkspacePosition pos){
        if (pos.getX()+8 > 15 || pos.getX()+8<0) return false;
        if (pos.getY()+8 > 15 || pos.getY()+8<0) return false;
        if (pos.getZ()+8 > 15 || pos.getZ()+8<0) return false;
        return true;
    }

    @Override
    public void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(@Nullable PlayerEntity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {

    }

    @Override
    public void playSoundFromEntity(@Nullable PlayerEntity source, Entity entity, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {

    }

    @Override
    public void createExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, ExplosionSourceType explosionSourceType, ParticleEffect smallParticle, ParticleEffect largeParticle, RegistryEntry<SoundEvent> soundEvent) {

    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public @Nullable Entity getEntityById(int id) {
        return null;
    }

    @Override
    public Collection<EnderDragonPart> getEnderDragonParts() {
        return List.of();
    }

    @Override
    public TickManager getTickManager() {
        return null;
    }

    @Override
    public @Nullable MapState getMapState(MapIdComponent id) {
        return null;
    }

    @Override
    public void putMapState(MapIdComponent id, MapState state) {

    }

    @Override
    public MapIdComponent increaseAndGetMapId() {
        return null;
    }

    @Override
    public void setBlockBreakingInfo(int entityId, BlockPos pos, int progress) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Override
    protected EntityLookup<Entity> getEntityLookup() {
        return null;
    }

    @Override
    public BrewingRecipeRegistry getBrewingRecipeRegistry() {
        return null;
    }

    @Override
    public FuelRegistry getFuelRegistry() {
        return null;
    }

    @Override
    public ChunkManager getChunkManager() {
        return null;
    }

    @Override
    public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) {

    }

    @Override
    public void emitGameEvent(RegistryEntry<GameEvent> event, Vec3d emitterPos, GameEvent.Emitter emitter) {

    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return List.of();
    }

    @Override
    public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public FeatureSet getEnabledFeatures() {
        return null;
    }

    @Override
    public float getBrightness(Direction direction, boolean shaded) {
        return 0;
    }

    @Override
    public QueryableTickScheduler<Block> getBlockTickScheduler() {
        return this.blockTickScheduler;
    }

    @Override
    public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
        return this.fluidTickScheduler;
    }

    @Override
    public void scheduleBlockTick(BlockPos pos, Block block, int delay) {
        this.getBlockTickScheduler().scheduleTick(this.createOrderedTick(pos, block, delay));
    }


    public static class WrapperWorldProperties implements MutableWorldProperties {

        public final WrapperEntity ENTITY;

        public WrapperWorldProperties(WrapperEntity entity) {
            this.ENTITY = entity;
        }

        @Override
        public void setSpawnPos(BlockPos pos, float angle) {}

        @Override
        public BlockPos getSpawnPos() {return BlockPos.ORIGIN;}

        @Override
        public float getSpawnAngle() {return 0;}

        @Override
        public long getTime() {return this.ENTITY.getWorld().getTime();}

        @Override
        public long getTimeOfDay() {return this.ENTITY.getWorld().getTimeOfDay();}

        @Override
        public boolean isThundering() {return this.ENTITY.getWorld().isThundering();}

        @Override
        public boolean isRaining() {return this.ENTITY.getWorld().isRaining();}

        @Override
        public void setRaining(boolean raining) {}

        @Override
        public boolean isHardcore() {return false;}

        @Override
        public Difficulty getDifficulty() {return this.ENTITY.getWorld().getDifficulty();}

        @Override
        public boolean isDifficultyLocked() {return false;}
    }
}
