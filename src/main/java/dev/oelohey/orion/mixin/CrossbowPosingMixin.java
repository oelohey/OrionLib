package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionItemComponents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowPosing.class)
public abstract class CrossbowPosingMixin {

	@Inject(at = @At("TAIL"), method = "charge")
	private static void orion$crossbow_pose(ModelPart holdingArm, ModelPart pullingArm, LivingEntity actor, boolean rightArmed, CallbackInfo ci) {
		if (actor.getMainHandStack().getOrDefault(OrionItemComponents.DOUBLE_HANDED, false)) {
			ModelPart modelPart = rightArmed ? holdingArm : pullingArm;
			ModelPart modelPart2 = rightArmed ? pullingArm : holdingArm;
			modelPart.yaw = rightArmed ? -0.3F : 0.4F;
			modelPart.pitch = -0.97079635F;
			modelPart2.pitch = modelPart.pitch;
			float f = (float) CrossbowItem.getPullTime(actor.getActiveItem(), actor);
			float g = MathHelper.clamp((float) actor.getItemUseTime(), 0.0F, f);
			float h = g / f;
			modelPart2.yaw = MathHelper.lerp(h, 1.0F, 1.1F) * (float) (rightArmed ? 1 : -1);
			modelPart2.pitch = MathHelper.lerp(h, modelPart2.pitch, (float) (-Math.PI / 2));
		}
    }
}