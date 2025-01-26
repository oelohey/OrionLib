package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CustomSubmersionTypeAccesor;
import dev.oelohey.orion.data_types.CustomSubmersionType;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Inject(at = @At("HEAD"), method = "renderSky", cancellable = true)
	private void orion$fog_sky(FrameGraphBuilder frameGraphBuilder, Camera camera, float tickDelta, Fog fog, CallbackInfo ci) {
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