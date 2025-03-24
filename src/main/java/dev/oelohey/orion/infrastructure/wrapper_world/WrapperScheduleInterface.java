package dev.oelohey.orion.infrastructure.wrapper_world;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrapperScheduleInterface extends ServerWorld {

    public Wrapper TARGET;

    public WrapperScheduleInterface(MinecraftServer server, ServerWorldProperties properties,  DimensionOptions dimensionOptions, LevelStorage.Session session) {
        super(server, null, session, properties, World.OVERWORLD, dimensionOptions, null, false, 0, List.of(), true, null);
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        return this.TARGET.setBlockState(pos, state, flags, maxUpdateDepth);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.TARGET.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.TARGET.getFluidState(pos);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.TARGET.getBlockEntity(pos);
    }
}
