package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CameraSetCameraAcessor;
import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.handler.ScreenshakeHandler;
import dev.oelohey.orion.infrastructure.ScreenshakeInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static org.joml.Math.lerp;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow @Final private MinecraftClient client;

	@Shadow @Final private DebugHud debugHud;

	@Inject(at = @At("HEAD"), method = "render")
	private void orion$inject_context_change(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        orion$change_context(context);
    }

	@Inject(at = @At("HEAD"), method = "renderMiscOverlays")
	private void orion$inject_context_changeMisc(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
		orion$change_context_matrix(context, -1.0f);
	}

	@Inject(at = @At("RETURN"), method = "renderMiscOverlays")
	private void orion$inject_context_changeMisc_RET(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
		orion$change_context_matrix(context, 1.0f);
	}

	@Inject(at = @At("HEAD"), method = "renderCrosshair")
	private void orion$inject_context_changeCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
		GameOptions gameOptions = this.client.options;
		if (this.debugHud.shouldShowDebugHud()) {
            assert this.client.player != null;
            if (!this.client.player.hasReducedDebugInfo() && !gameOptions.getReducedDebugInfo().getValue()) {
                orion$change_context_matrix(context, -1.0f);
            }
        }
	}

	@Inject(at = @At("RETURN"), method = "renderCrosshair")
	private void orion$inject_context_changeCrosshair_RET(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
		GameOptions gameOptions = this.client.options;
		if (this.debugHud.shouldShowDebugHud()) {
			assert this.client.player != null;
			if (!this.client.player.hasReducedDebugInfo() && !gameOptions.getReducedDebugInfo().getValue()) {
				orion$change_context_matrix(context, 1.0f);
			}
		}
	}


	@Inject(at = @At("RETURN"), method = "render")
	private void orion$inject_context_change_fix(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
		context.getMatrices().pop();
	}


	@Unique
	private void orion$change_context(DrawContext context) {
		context.getMatrices().push();
		orion$change_context_matrix(context, 1.0f);
	}

	@Unique
	private void orion$change_context_matrix(DrawContext context, float multiplier){
		if (this.client.gameRenderer.getCamera() instanceof CameraSetCameraAcessor cameraSet) {
			if (this.client.gameRenderer.getCamera().getFocusedEntity() instanceof ScreenshakeNBTAcessor nbtAcessor) {
				List<ScreenshakeInstance> screenshakeInstances = nbtAcessor.orion$getInstances();
				if (!screenshakeInstances.isEmpty()) {
					float offsetSIDE = lerp(cameraSet.orion$getOffsetYawOLD(), cameraSet.orion$getOffsetYaw(), ScreenshakeHandler.getLerp(cameraSet));
					float offsetHEIGHT = lerp(cameraSet.orion$getOffsetPitchOLD(), cameraSet.orion$getOffsetPitch(), ScreenshakeHandler.getLerp(cameraSet));

					context.getMatrices().translate(offsetSIDE*10*multiplier, offsetHEIGHT*-10*multiplier, 0);
				}
			}
		}
	}

}