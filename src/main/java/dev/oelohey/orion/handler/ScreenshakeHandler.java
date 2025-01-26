package dev.oelohey.orion.handler;

import dev.oelohey.orion.accesor.CameraSetCameraAcessor;
import dev.oelohey.orion.accesor.ScreenshakeNBTAcessor;
import dev.oelohey.orion.internal_util.ScreenshakeInstance;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;

import java.util.List;

import static org.joml.Math.lerp;

public class ScreenshakeHandler {
    public static void registerHandler() {

    }

    public static void cameraTick(Camera camera){
        PlayerEntity player = (PlayerEntity) camera.getFocusedEntity();

        if (player instanceof ScreenshakeNBTAcessor nbtAcessor) {
            List<ScreenshakeInstance> screenshakeInstances = nbtAcessor.orion$getInstances();
            if (!screenshakeInstances.isEmpty()){
                ScreenshakeInstance instanceToApply = null;
                for (ScreenshakeInstance instance : screenshakeInstances) {
                    if (instance != null) {
                        if (instance.getIntensityX(camera.getPos()) + instance.getIntensityY(camera.getPos()) != 0) {
                            instanceToApply = instance;
                        }
                    }
                }
                if (camera instanceof CameraSetCameraAcessor cameraSet) {
                    assert instanceToApply != null;
                    applyCameraShake(cameraSet, camera, instanceToApply);
                }
            }
        }
    }

    private static void applyCameraShake(CameraSetCameraAcessor cameraSet, Camera camera, ScreenshakeInstance screenshakeInstance){
        float timeMaxPerShake = 3;
        if (screenshakeInstance != null){
            timeMaxPerShake = screenshakeInstance.timeMaxPerShake;
        }
        cameraSet.orion$setTimeMax(timeMaxPerShake);
        float offsetSIDE = lerp(cameraSet.orion$getOffsetYawOLD(), cameraSet.orion$getOffsetYaw(), getLerp(cameraSet));
        float offsetHEIGHT = lerp(cameraSet.orion$getOffsetPitchOLD(), cameraSet.orion$getOffsetPitch(), getLerp(cameraSet));

        cameraSet.orion$moveBy(0, offsetHEIGHT, offsetSIDE);

        if (getLerp(cameraSet) >= 1){
            if (cameraSet.orion$getOffsetYaw() == 0.0f){
                setToOLD(cameraSet);
                randomizeOffset(camera, screenshakeInstance);
            }else {
                setToOLD(cameraSet);
            }
            cameraSet.orion$resetTicker(0);
        }
    }

    private static void randomizeOffset(Camera camera, ScreenshakeInstance screenshakeInstance){
        Random random = camera.getFocusedEntity().getWorld().getRandom();
        if (camera instanceof CameraSetCameraAcessor cameraSet){
            cameraSet.orion$setOffsetYaw(((random.nextFloat()-0.5f)*0.4f)* screenshakeInstance.getIntensityX(camera.getPos()));
            cameraSet.orion$setOffsetPitch(((random.nextFloat()-0.5f)*0.15f)* screenshakeInstance.getIntensityY(camera.getPos()));
        }
    }

    private static void setToOLD(CameraSetCameraAcessor cameraSet){
        cameraSet.orion$setOffsetYawOLD(cameraSet.orion$getOffsetYaw());
        cameraSet.orion$setOffsetPitchOLD(cameraSet.orion$getOffsetPitch());
        cameraSet.orion$setOffsetYaw(0.0f);
        cameraSet.orion$setOffsetPitch(0.0f);
    }

    public static float getLerp(CameraSetCameraAcessor cameraSet){
        return cameraSet.orion$getTicker() / cameraSet.orion$getTimeMax();
    }
}
