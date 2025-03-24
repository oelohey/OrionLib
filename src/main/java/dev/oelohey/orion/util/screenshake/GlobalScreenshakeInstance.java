package dev.oelohey.orion.util.screenshake;

import dev.oelohey.orion.infrastructure.ScreenshakeInstance;
import net.minecraft.util.math.Vec3d;

public class GlobalScreenshakeInstance extends ScreenshakeInstance {

    /**
     * Class meant to apply a global screenshake
     * to the players that have been applied with it
     */

    public static GlobalScreenshakeInstance createInstance(int duration, float frequency, float intensity, boolean diminishIntensity){
        return createInstance(duration, frequency, intensity, intensity, diminishIntensity);
    }

    public static GlobalScreenshakeInstance createInstance(int duration, float frequency, float intensityX, float intensityY, boolean diminishIntensity){
        GlobalScreenshakeInstance instance = new GlobalScreenshakeInstance();
        instance.duration = duration;
        instance.maxDuration = duration;
        instance.timeMaxPerShake = frequency;
        instance.intensityX = intensityX;
        instance.intensityY = intensityY;
        instance.diminishIntensity = diminishIntensity;
        return instance;
    }

    public float getIntensityX(Vec3d loc){
        float ret = this.intensityX;
        if (this.diminishIntensity){
            ret*= ((float) this.duration) / ((float)this.maxDuration);
        }
        return ret;
    }

    public float getIntensityY(Vec3d loc){
        float ret = this.intensityY;
        if (this.diminishIntensity){
            ret*= ((float) this.duration) / ((float)this.maxDuration);
        }
        return ret;
    }
}
