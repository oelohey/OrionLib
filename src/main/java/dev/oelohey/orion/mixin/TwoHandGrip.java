package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionItemComponents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class TwoHandGrip {
	@Inject(at = @At("RETURN"), method = "getArmPose", cancellable = true)
	private static void orion$two_handed_hold(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
		if (player.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
			if (!player.isUsingItem()) {
				cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
			}
		}
	}
}