package dev.oelohey.orion.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.oelohey.orion.command.screenshake_util.CargoTest;
import dev.oelohey.orion.command.screenshake_util.ScreenshakeCommandUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;

public class CommandRegistry {

    public static void commandRegistry(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("cargo_test")
                .requires(source -> source.hasPermissionLevel(1))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("pos1", BlockPosArgumentType.blockPos())
                                .then(CommandManager.argument("pos2", BlockPosArgumentType.blockPos())
                                        .executes(CargoTest::cargoTest))))))

        ;
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("screenshake")
                .requires(source -> source.hasPermissionLevel(1))
                        .then(CommandManager.literal("reset")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .executes(ScreenshakeCommandUtil::screenshakeCommandRESET)))
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("duration", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                                .executes(ScreenshakeCommandUtil::screenshakeCommandADD_DUR)
                                                .then(CommandManager.argument("frequency", FloatArgumentType.floatArg(0.1f, Float.MAX_VALUE))
                                                        .executes(ScreenshakeCommandUtil::screenshakeCommandADD_FREQ)
                                                        .then(CommandManager.argument("intensity_x", FloatArgumentType.floatArg(0.1f, 100))
                                                                .executes(ScreenshakeCommandUtil::screenshakeCommandADD_INTENSITYX)
                                                                .then(CommandManager.argument("intensity_y", FloatArgumentType.floatArg(0.1f, 100))
                                                                        .executes(ScreenshakeCommandUtil::screenshakeCommandADD_INTENSITYY)
                                                                        .then(CommandManager.argument("diminish_intensity", BoolArgumentType.bool())
                                                                                .executes(ScreenshakeCommandUtil::screenshakeCommandADD)
                                                                                .then(CommandManager.argument("origin", Vec3ArgumentType.vec3())
                                                                                        .then(CommandManager.argument("max_distance", FloatArgumentType.floatArg(0.1f, Float.MAX_VALUE))
                                                                                                .executes(ScreenshakeCommandUtil::screenshakeCommandADD_POSITIONED))
                                                                                )))))))
                        )));
    }
}
