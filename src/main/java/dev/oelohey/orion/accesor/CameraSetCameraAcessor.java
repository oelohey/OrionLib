package dev.oelohey.orion.accesor;

import net.minecraft.util.math.Vec3d;

public interface CameraSetCameraAcessor {
    void orion$setPos(Vec3d pos);
    void orion$moveBy(float f, float g, float h);

    float orion$getOffsetYaw();
    float orion$getOffsetPitch();
    void orion$setOffsetYaw(float offsetYaw);
    void orion$setOffsetPitch(float offsetPitch);

    float orion$getOffsetYawOLD();
    float orion$getOffsetPitchOLD();
    void orion$setOffsetYawOLD(float offsetYaw);
    void orion$setOffsetPitchOLD(float offsetPitch);

    float orion$getTicker();
    void orion$resetTicker(float value);

    float orion$getTimeMax();
    void orion$setTimeMax(float maxTime);
}
