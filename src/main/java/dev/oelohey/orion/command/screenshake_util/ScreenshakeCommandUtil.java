package dev.oelohey.orion.command.screenshake_util;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import dev.oelohey.orion.util.screenshake.PositionedScreenshakeInstance;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public class ScreenshakeCommandUtil {
    public static int screenshakeCommandADD(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OrionLib.LOGGER.info("TEST_1");
        Collection<ServerPlayerEntity> playerList =  EntityArgumentType.getPlayers(context, "targets");
        OrionLib.LOGGER.info("TEST_2");
        int duration = IntegerArgumentType.getInteger(context, "duration");
        OrionLib.LOGGER.info("TEST_3");
        float frequency = FloatArgumentType.getFloat(context, "frequency");
        OrionLib.LOGGER.info("TEST_4");
        float intensityX = FloatArgumentType.getFloat(context, "intensity_x");
        OrionLib.LOGGER.info("TEST_5");
        float intensityY = FloatArgumentType.getFloat(context, "intensity_y");
        OrionLib.LOGGER.info("TEST_6");
        boolean diminishIntensity = BoolArgumentType.getBool(context, "diminish_intensity");
        OrionLib.LOGGER.info("TEST_7");

        for (ServerPlayerEntity serverPlayerEntity : playerList) {
            if (serverPlayerEntity instanceof ScreenshakeNBTAcessor nbtAcessor){
                nbtAcessor.orion$addInstance(GlobalScreenshakeInstance.createInstance(duration, frequency, intensityX, intensityY, diminishIntensity));
            }
        }

        context.getSource().sendFeedback(() -> Text.of("Applied Global Screenshake to").copy().append(playerList.iterator().next().getName()), true);
        return 1;
    }

    public static int screenshakeCommandADD_POSITIONED(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> playerList =  EntityArgumentType.getPlayers(context, "targets");
        int duration = IntegerArgumentType.getInteger(context, "duration");
        float frequency = FloatArgumentType.getFloat(context, "frequency");
        float intensityX = FloatArgumentType.getFloat(context, "intensity_x");
        float intensityY = FloatArgumentType.getFloat(context, "intensity_y");
        boolean diminishIntensity = BoolArgumentType.getBool(context, "diminish_intensity");

        Vec3d origin = Vec3ArgumentType.getVec3(context, "origin");
        float maxDistance = FloatArgumentType.getFloat(context, "max_distance");

        for (ServerPlayerEntity serverPlayerEntity : playerList) {
            if (serverPlayerEntity instanceof ScreenshakeNBTAcessor nbtAcessor){
                nbtAcessor.orion$addInstance(PositionedScreenshakeInstance.createInstance(duration, frequency, intensityX, intensityY, diminishIntensity, origin, maxDistance));
            }
        }

        context.getSource().sendFeedback(() -> Text.of("Applied Positioned Screenshake to").copy().append(playerList.iterator().next().getName()), true);
        return 1;
    }

    public static int screenshakeCommandRESET(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> playerList =  EntityArgumentType.getPlayers(context, "targets");
        for (ServerPlayerEntity player : playerList){
            if (player instanceof ScreenshakeNBTAcessor nbtAcessor){
                nbtAcessor.orion$resetInstances();
            }
        }

        context.getSource().sendFeedback(() -> Text.of("Reset Screenshake Instances for ").copy().append(playerList.iterator().next().getName()), true);
        return 1;
    }
}
