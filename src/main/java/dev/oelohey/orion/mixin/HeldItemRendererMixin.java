package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CameraSetCameraAcessor;
import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.handler.ScreenshakeHandler;
import dev.oelohey.orion.infrastructure.ScreenshakeInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static org.joml.Math.lerp;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At("HEAD"), method = "renderArm")
	private void orion$inject_context_change(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, CallbackInfo ci){
        orion$change_matrix(matrices);

    }

	@Inject(at = @At("RETURN"), method = "renderArm")
	private void orion$inject_context_change_fix(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, CallbackInfo ci){
		matrices.pop();
	}

	@Inject(at = @At("HEAD"), method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V")
	private void orion$inject_context_change_item(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci){
		orion$change_matrix(matrices);

	}

	@Inject(at = @At("RETURN"), method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V")
	private void orion$inject_context_change_fix(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci){
		matrices.pop();
	}

	@Inject(at = @At("HEAD"), method = "renderMapInBothHands")
	private void orion$inject_context_change_map(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress, CallbackInfo ci){
		orion$change_matrix(matrices);

	}

	@Inject(at = @At("RETURN"), method = "renderMapInBothHands")
	private void orion$inject_context_change_map_fix(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress, CallbackInfo ci){
		matrices.pop();
	}

	@Inject(at = @At("HEAD"), method = "renderMapInOneHand")
	private void orion$inject_context_change_map_one(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, Arm arm, float swingProgress, ItemStack stack, CallbackInfo ci){
		orion$change_matrix(matrices);

	}

	@Inject(at = @At("RETURN"), method = "renderMapInOneHand")
	private void orion$inject_context_change_map_one_fix(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, Arm arm, float swingProgress, ItemStack stack, CallbackInfo ci){
		matrices.pop();
	}

	@Inject(at = @At("HEAD"), method = "renderFirstPersonMap")
	private void orion$inject_context_change_first_map(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int swingProgress, ItemStack stack, CallbackInfo ci){
		orion$change_matrix(matrices);

	}

	@Inject(at = @At("RETURN"), method = "renderFirstPersonMap")
	private void orion$inject_context_change_first_map_fix(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int swingProgress, ItemStack stack, CallbackInfo ci){
		matrices.pop();
	}

	@Inject(at = @At("HEAD"), method = "renderFirstPersonItem")
	private void orion$inject_context_change_first_item(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
		orion$change_matrix(matrices);

	}

	@Inject(at = @At("RETURN"), method = "renderFirstPersonItem")
	private void orion$inject_context_change_first_item_fix(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
		matrices.pop();
	}


	@Unique
	private void orion$change_matrix(MatrixStack matrixStack) {
		if (this.client.gameRenderer.getCamera() instanceof CameraSetCameraAcessor cameraSet) {
			matrixStack.push();
			if (this.client.gameRenderer.getCamera().getFocusedEntity() instanceof ScreenshakeNBTAcessor nbtAcessor) {
				List<ScreenshakeInstance> screenshakeInstances = nbtAcessor.orion$getInstances();
				if (!screenshakeInstances.isEmpty()) {
					float offsetSIDE = lerp(cameraSet.orion$getOffsetYawOLD(), cameraSet.orion$getOffsetYaw(), ScreenshakeHandler.getLerp(cameraSet));
					float offsetHEIGHT = lerp(cameraSet.orion$getOffsetPitchOLD(), cameraSet.orion$getOffsetPitch(), ScreenshakeHandler.getLerp(cameraSet));

					matrixStack.translate(offsetSIDE * 0.3, offsetHEIGHT * 0.3, 0);
				}
			}
		}
	}

}