package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionItemComponents;
import dev.oelohey.orion.internal_util.UpdateBipedRenderStateUtilOrion;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class TwoHandGrip extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel>{


	public TwoHandGrip(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(at = @At("RETURN"), method = "getArmPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/util/Arm;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;", cancellable = true)
	private static void orion$two_handed_hold(AbstractClientPlayerEntity player, Arm arm, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
		if (player.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
			if (!player.isUsingItem()) {
				cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V")
	private void orion$updateRenderStateFixer(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci){
		UpdateBipedRenderStateUtilOrion.updateBipedRenderStateDoubleHanded(abstractClientPlayerEntity, playerEntityRenderState, f, this.itemModelResolver, abstractClientPlayerEntity.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false));
	}
}