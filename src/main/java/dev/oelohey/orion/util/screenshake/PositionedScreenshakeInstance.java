package dev.oelohey.orion.util.screenshake;

import dev.oelohey.orion.internal_util.ScreenshakeInstance;
import net.minecraft.util.math.Vec3d;

public class PositionedScreenshakeInstance extends ScreenshakeInstance {

    /**
     * Class meant to apply a positioned screenshake
     * to the players that have been applied with it
     */

    public Vec3d origin;
    public float maxDistance;

    public static PositionedScreenshakeInstance createInstance(int duration, float frequency, float intensity, boolean diminishIntensity, Vec3d origin, float maxDistance){
        return createInstance(duration, frequency, intensity, intensity, diminishIntensity, origin, maxDistance);
    }

    public static PositionedScreenshakeInstance createInstance(int duration, float frequency, float intensityX, float intensityY, boolean diminishIntensity, Vec3d origin, float maxDistance){
        PositionedScreenshakeInstance instance = new PositionedScreenshakeInstance();
        instance.duration = duration;
        instance.maxDuration = duration;
        instance.timeMaxPerShake = frequency;
        instance.intensityX = intensityX;
        instance.intensityY = intensityY;
        instance.diminishIntensity = diminishIntensity;
        instance.origin = origin;
        instance.maxDistance = maxDistance;
        return instance;
    }

    @Override
    public float getIntensityX(Vec3d loc){
        float ret = super.getIntensityX(loc);
        if (this.diminishIntensity){
            ret*= ((float) this.duration) / ((float)this.maxDuration);
        }
        double dist = Math.max((maxDistance-origin.distanceTo(loc)), 0);
        if ((int) dist == 0){
            return 0.00001f;
        }
        dist = dist / maxDistance;
        ret *= (float) dist;
        return ret;
    }

    @Override
    public float getIntensityY(Vec3d loc){
        float ret = super.getIntensityY(loc);
        if (this.diminishIntensity){
            ret*= ((float) this.duration) / ((float)this.maxDuration);
        }
        double dist = Math.max((maxDistance-origin.distanceTo(loc)), 0);
        if ((int) dist == 0){
            return 0.00001f;
        }
        dist = dist / maxDistance;
        ret *= (float) dist;
        return ret;
    }
}
