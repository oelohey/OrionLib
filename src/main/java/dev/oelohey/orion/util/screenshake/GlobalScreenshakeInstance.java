package dev.oelohey.orion.util.screenshake;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.internal_util.ScreenshakeInstance;
import net.minecraft.util.math.Vec3d;

import java.util.Date;

public class GlobalScreenshakeInstance extends ScreenshakeInstance {

    /**
     * Class meant to apply a global screenshake
     * to the players that have been applied with it
     */

    public static GlobalScreenshakeInstance createInstance(int duration, float timeMaxPerShake, float intensity, boolean diminishIntensity){
        return createInstance(duration, timeMaxPerShake, intensity, intensity, diminishIntensity);
    }

    public static GlobalScreenshakeInstance createInstance(int duration, float timeMaxPerShake, float intensityX, float intensityY, boolean diminishIntensity){
        GlobalScreenshakeInstance instance = new GlobalScreenshakeInstance();
        instance.duration = duration;
        OrionLib.LOGGER.info(String.valueOf(instance.duration));
        instance.maxDuration = duration;
        instance.timeMaxPerShake = timeMaxPerShake;
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
