package dev.oelohey.orion.infrastructure.wrapper_world.chunkspace;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.infrastructure.wrapper_world.Wrapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Chunkspace {

    private final Wrapper WORLD;

    private final ChunkspacePosition POSITION;

    private final Map<BlockPos, BlockState> BLOCKSTATES = new Object2ObjectOpenHashMap<>();
    private final Map<BlockPos, BlockEntity> BLOCKENTITIES = new Object2ObjectOpenHashMap<>();

    public Chunkspace(Wrapper world, ChunkspacePosition position) {
        WORLD = world;
        POSITION = position;
    }

    public void tick(){
        for (BlockPos pos : BLOCKSTATES.keySet()){
            BlockState state = BLOCKSTATES.get(pos);

            MinecraftClient.getInstance().particleManager.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK_MARKER, state), WORLD.ENTITY.getX()+pos.getX(), WORLD.ENTITY.getY()+pos.getY(), WORLD.ENTITY.getZ()+pos.getZ(), 0,0,0);
        }
    }

    @Nullable
    public BlockState setBlockState(BlockPos pos, BlockState state, boolean moved){
        if (state.isAir()) {
            //REMOVE BLOCK
            if (this.blockPosOccupied(pos)) this.BLOCKSTATES.remove(toLocalBlockPos(pos));
            return null;
        }

        if (blockPosOccupied(pos)) {
            BlockState blockState = getBlockState(pos);
            blockState.onStateReplaced(WORLD, pos, state, moved);
            if (blockState.hasBlockEntity()) this.removeBlockEntity(pos);
        }

        state.onBlockAdded(WORLD, pos, state, moved);

        if (state.hasBlockEntity()){
            BlockEntity blockEntity = null;
            if (BLOCKENTITIES.containsKey(pos)) blockEntity = this.BLOCKENTITIES.get(pos);
            if (blockEntity != null && !blockEntity.supports(state)){
                this.removeBlockEntity(pos);
                blockEntity = null;
            }

            if (blockEntity == null){
                BlockEntity newEntity = ((BlockEntityProvider) state.getBlock()).createBlockEntity(pos, state);
                this.BLOCKENTITIES.put(pos, newEntity);
            }else {
                blockEntity.setCachedState(state);
            }
        }

        this.BLOCKSTATES.put(toLocalBlockPos(pos), state);

        return state;
    }

    @Nullable
    public BlockState getBlockState(BlockPos pos){
        if (!blockPosOccupied(pos)) return Blocks.AIR.getDefaultState();
        return this.BLOCKSTATES.get(toLocalBlockPos(pos));
    }

    @Nullable
    public FluidState getFluidState(BlockPos pos){
        if (!blockPosOccupied(pos)) return Fluids.EMPTY.getDefaultState();
        return this.BLOCKSTATES.get(toLocalBlockPos(pos)).getFluidState();
    }

    @Nullable
    public ChunkspacePosition getChunkPos(){
        return POSITION;
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos pos){
        if (this.BLOCKENTITIES.containsKey(pos)) return null;
        return this.BLOCKENTITIES.get(pos);
    }

    public void setBlockEntity(BlockEntity blockEntity){
        BlockPos pos = blockEntity.getPos();

        BlockState blockState = this.getBlockState(pos);

        if (!blockState.hasBlockEntity()){
            OrionLib.LOGGER.info("Trying to set block entity {} at position {} inside a Wrapper, but state {} does not allow it", blockEntity, pos, blockState);
        }else{
            BlockState blockState2 = blockEntity.getCachedState();

            if (blockState != blockState2){
                if (!blockEntity.getType().supports(blockState)) {
                    OrionLib.LOGGER.warn("Trying to set block entity {} at position {} inside a Wrapper, but state {} does not allow it", blockEntity, pos, blockState);
                    return;
                }

                if (blockState.getBlock() != blockState2.getBlock()) {
                    OrionLib.LOGGER.warn("Block state mismatch on block entity {} in position {} inside a Wrapper, {} != {}, updating", blockEntity, pos, blockState, blockState2);
                }

                blockEntity.setCachedState(blockState);
            }
        }

        blockEntity.setWorld(this.WORLD);
        blockEntity.cancelRemoval();
        BlockEntity blockEntity2 = this.BLOCKENTITIES.put(pos, blockEntity);
        if (blockEntity2 != null && blockEntity2 != blockEntity){
            blockEntity2.markRemoved();
        }


    }

    public void removeBlockEntity(BlockPos pos){
        BlockEntity entity = this.BLOCKENTITIES.remove(pos);
        if (entity != null) entity.markRemoved();
    }

    public boolean blockPosOccupied(BlockPos pos){
        return this.BLOCKSTATES.containsKey(toLocalBlockPos(pos));
    }

    private BlockPos toLocalBlockPos(BlockPos pos){
        return new BlockPos(
                pos.getX() % 16,
                pos.getY() % 16,
                pos.getZ() % 16
        );
    }
}
