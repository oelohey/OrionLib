package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CustomSubmersionTypeAccesor;
import dev.oelohey.orion.data_types.CustomSubmersionType;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@Inject(at = @At("RETURN"), method = "applyFog", cancellable = true)
	private static void orion$fog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {

		float fogStart;
		float fogEnd;
		FogShape fogShape = FogShape.CYLINDER;

		Entity entity = camera.getFocusedEntity();

		if (camera instanceof CustomSubmersionTypeAccesor customSubmersionTypeAccesor) {
			String submersionType = customSubmersionTypeAccesor.orion$customSubmersionType();
			for (CustomSubmersionType customSubmersionType : SubmersionTypeDataHandler.customSubmersionTypes) {
				String currentSubmersionType = customSubmersionType.getSubmersionTypeName();
				if (Objects.equals(currentSubmersionType, submersionType)){
					if (entity.isSpectator()){
						fogStart = getValueOfCurrent(viewDistance, customSubmersionType, true, "fogStart");
						fogEnd = getValueOfCurrent(viewDistance, customSubmersionType, true, "fogEnd");
					} else {
						fogStart = getValueOfCurrent(viewDistance, customSubmersionType, false, "fogStart");
						fogEnd = getValueOfCurrent(viewDistance, customSubmersionType, false, "fogEnd");
					}
					color.x = customSubmersionType.getRedValue();
					color.y = customSubmersionType.getGreenValue();
					color.z = customSubmersionType.getBlueValue();

					cir.setReturnValue(new Fog(fogStart, fogEnd, fogShape, color.x, color.y, color.z, color.w));
				}
			}
		}

	}

	@Unique
	private static float getValueOfCurrent(float viewDistance, CustomSubmersionType submersionType, boolean isSpectator, String getter){
		float value = 0.0f;
		if (isSpectator){
			if (Objects.equals(getter, "fogStart")){
				value = submersionType.getFogStartSpectator();
				if (submersionType.multipliesViewDistanceFogStartSpectator()){
					value = viewDistance*value;
				}
			}
			if (Objects.equals(getter, "fogEnd")){
				value = submersionType.getFogEndSpectator();
				if (submersionType.multipliesViewDistanceFogEndSpectator()){
					value = viewDistance*value;
				}
			}
		}else{
			if (Objects.equals(getter, "fogStart")){
				value = submersionType.getFogStart();
				if (submersionType.multipliesViewDistanceFogStart()){
					value = viewDistance*value;
				}
			}
			if (Objects.equals(getter, "fogEnd")){
				value = submersionType.getFogEnd();
				if (submersionType.multipliesViewDistanceFogEnd()){
					value = viewDistance*value;
				}
			}

		}
        return value;
    }
}