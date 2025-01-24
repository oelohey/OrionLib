package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.CameraSetCameraAcessor;
import dev.oelohey.orion.accesor.CustomSubmersionTypeAccesor;
import dev.oelohey.orion.data_types.CustomSubmersionType;
import dev.oelohey.orion.handler.ScreenshakeHandler;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin implements CustomSubmersionTypeAccesor, CameraSetCameraAcessor {

	@Shadow private BlockView area;

	@Shadow @Final private BlockPos.Mutable blockPos;

	@Shadow public abstract Vec3d getPos();

	@Shadow protected abstract void setRotation(float yaw, float pitch);

	@Shadow public abstract Quaternionf getRotation();

	@Shadow protected abstract void setPos(Vec3d pos);

	@Shadow public abstract Vector3f getDiagonalPlane();

	@Shadow protected abstract void moveBy(float f, float g, float h);

	@Unique
	public float offsetYaw;

	@Unique
	public float offsetPitch;

	@Unique
	public float offsetYawOLD;

	@Unique
	public float offsetPitchOLD;

	@Unique
	public float timeToLerp = 2;

	@Unique
	public float ticker;

	@Unique
	public String getCustomSubmersionType(){

		BlockState blockState = this.area.getBlockState(this.blockPos);

		for (CustomSubmersionType submersionType : SubmersionTypeDataHandler.customSubmersionTypes){
			if (blockState.isOf(submersionType.blockForApply())){
				if (submersionType.passCondition(blockState, blockPos, this.getPos())){
					return submersionType.getSubmersionTypeName();
				}
			}
		}
        return "NONE";
    }

	@Override
	public String orion$customSubmersionType() {
		return getCustomSubmersionType();
	}

	@Override
	public void orion$setPos(Vec3d pos){
		this.setPos(pos);
	}

	@Override
	public void orion$moveBy(float f, float g, float h){
		this.moveBy(f, g ,h);
	}

	@Override
	public float orion$getOffsetYaw(){
		return this.offsetYaw;
	}
	@Override
	public float orion$getOffsetPitch(){
		return this.offsetPitch;
	}
	@Override
	public void orion$setOffsetYaw(float offsetYaw){
		this.offsetYaw = offsetYaw;
	}
	@Override
	public void orion$setOffsetPitch(float offsetPitch){
		this.offsetPitch = offsetPitch;
	}

	@Override
	public float orion$getOffsetYawOLD(){
		return this.offsetYawOLD;
	}
	@Override
	public float orion$getOffsetPitchOLD(){
		return this.offsetPitchOLD;
	}
	@Override
	public void orion$setOffsetYawOLD(float offsetYaw){
		this.offsetYawOLD = offsetYaw;
	}
	@Override
	public void orion$setOffsetPitchOLD(float offsetPitch){
		this.offsetPitchOLD = offsetPitch;
	}
	@Override
	public float orion$getTicker(){
		return this.ticker;
	}
	@Override
	public void orion$resetTicker(float value){
		this.ticker = value;
	}

	@Override
	public float orion$getTimeMax(){
		return this.timeToLerp;
	}

	@Override
	public void orion$setTimeMax(float maxTime){
		this.timeToLerp = maxTime;
	}

	@Inject(at = @At("RETURN"), method = "update")
	private void orion$tick_addition(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		this.ticker = this.ticker+tickDelta;
		Camera camera = (Camera) (Object) this;
		ScreenshakeHandler.cameraTick(camera);
	}
}