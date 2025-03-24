package dev.oelohey.orion.infrastructure;

import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import dev.oelohey.orion.util.screenshake.PositionedScreenshakeInstance;
import net.minecraft.util.math.Vec3d;

public class ScreenshakeInstance {

    /**
     * CLASS NOT MEANT FOR USAGE
     * <p>
     * Instead, use its subclasses like:
     * @see PositionedScreenshakeInstance
     * @see GlobalScreenshakeInstance
     */

    public int duration;
    public int maxDuration;
    public float timeMaxPerShake;

    public float intensityX;
    public float intensityY;


    public boolean diminishIntensity;

    public float getIntensityX(Vec3d loc){
        return this.intensityX;
    }

    public float getIntensityY(Vec3d loc){
        return this.intensityY;
    }
}
