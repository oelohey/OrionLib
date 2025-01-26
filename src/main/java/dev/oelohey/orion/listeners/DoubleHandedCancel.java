package dev.oelohey.orion.listeners;

import dev.oelohey.orion.register.OrionItemComponents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class DoubleHandedCancel {
    public static void cancelCallbacks(){
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (player.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
                if (!player.getOffHandStack().isOf(Items.AIR)) {
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
                if (!player.getOffHandStack().isOf(Items.AIR)) {
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
    }
}
