package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CustomSubmersionTypeAccesor;
import dev.oelohey.orion.data_types.CustomSubmersionType;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderSky", cancellable = true)
	private void orion$fog_sky(Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
		if (camera instanceof CustomSubmersionTypeAccesor customSubmersionTypeAccesor){
			String customSubmersion = customSubmersionTypeAccesor.orion$customSubmersionType();
			for (CustomSubmersionType submersionType : SubmersionTypeDataHandler.customSubmersionTypes){
				if (customSubmersion.equals(submersionType.getSubmersionTypeName())){
					if (!submersionType.renderSky()){
						ci.cancel();
					}
				}
			}
		}


	}

}