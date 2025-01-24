package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionItemComponents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class TwoHandGripArmPose<T extends LivingEntity> extends AnimalModel<T> {
	@Shadow @Final public ModelPart leftArm;

	@Inject(at = @At("TAIL"), method = "animateArms")
	private void orion$two_handed_grip_pose(T entity, float animationProgress, CallbackInfo ci) {
		BipedEntityModel player = (BipedEntityModel) (Object) this;

		if (entity.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
			if (!(this.handSwingProgress <= 0.0F)) {
				ModelPart modelPart = player.leftArm;
				float f = this.handSwingProgress;

				f = 1.0F - this.handSwingProgress;
				f *= f;
				f *= f;
				f = 1.0F - f;
				float g = MathHelper.sin(f * (float) Math.PI);
				float h = MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -(player.head.pitch - 0.7F) * 0.75F;
				modelPart.pitch -= g * 1.2F + h;
				modelPart.yaw = modelPart.yaw + player.body.yaw * 2.0F;
				modelPart.roll = modelPart.roll + MathHelper.sin(this.handSwingProgress * (float) Math.PI) * -0.4F;
			}
		}
	}
}