package dev.oelohey.orion.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.oelohey.orion.accesor.CustomSubmersionTypeAccesor;
import dev.oelohey.orion.data_types.CustomSubmersionType;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@Shadow private static float red;

	@Shadow private static float green;

	@Shadow private static float blue;

	@Shadow private static long lastWaterFogColorUpdateTime;

	@Inject(at = @At("RETURN"), method = "applyFog")
	private static void orion$fog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {

		Entity entity = camera.getFocusedEntity();

		if (camera instanceof CustomSubmersionTypeAccesor customSubmersionTypeAccesor) {
			String submersionType = customSubmersionTypeAccesor.orion$customSubmersionType();
			for (CustomSubmersionType customSubmersionType : SubmersionTypeDataHandler.customSubmersionTypes) {
				String currentSubmersionType = customSubmersionType.getSubmersionTypeName();
				if (Objects.equals(currentSubmersionType, submersionType)){
					if (entity.isSpectator()){
						RenderSystem.setShaderFogStart(getValueOfCurrent(viewDistance, customSubmersionType, true, "fogStart"));
						RenderSystem.setShaderFogEnd(getValueOfCurrent(viewDistance, customSubmersionType, true, "fogEnd"));
					} else {
						RenderSystem.setShaderFogStart(getValueOfCurrent(viewDistance, customSubmersionType, false, "fogStart"));
						RenderSystem.setShaderFogEnd(getValueOfCurrent(viewDistance, customSubmersionType, false, "fogEnd"));
					}
				}
			}
		}

	}

	@Inject(at = @At("RETURN"), method = "render")
	private static void orion$fog_color(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {

		if (camera instanceof CustomSubmersionTypeAccesor customSubmersionTypeAccesor) {
			String submersionType = customSubmersionTypeAccesor.orion$customSubmersionType();
			for (CustomSubmersionType customSubmersionType : SubmersionTypeDataHandler.customSubmersionTypes) {
				String currentSubmersionType = customSubmersionType.getSubmersionTypeName();
				if (Objects.equals(currentSubmersionType, submersionType)){
					red = customSubmersionType.getRedValue();
					green = customSubmersionType.getGreenValue();
					blue = customSubmersionType.getBlueValue();
					lastWaterFogColorUpdateTime = -1L;
					RenderSystem.clearColor(red, green, blue, 0.0F);
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