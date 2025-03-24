package dev.oelohey.orion.command.screenshake_util;

import com.mojang.brigadier.context.CommandContext;
import dev.oelohey.orion.entity.WrapperEntity;
import dev.oelohey.orion.internal_util.WindlassCargoHelper;
import net.minecraft.block.FallingBlock;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CargoTest {
    public static int cargoTest(CommandContext<ServerCommandSource> context){
        BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
        BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
        WrapperEntity cargo = WindlassCargoHelper.createWindlassCargo(context.getSource().getWorld(), pos1, pos2, true);
        context.getSource().getWorld().toServerWorld().spawnEntity(cargo);
        context.getSource().sendFeedback(() -> Text.of(cargo.toString()), true);
        cargo.calculateDimensions();
        return 1;
    }
}
