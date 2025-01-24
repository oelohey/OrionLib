package dev.oelohey.orion.data_types;

import dev.oelohey.orion.util.screenshake.GlobalScreenshakeInstance;
import dev.oelohey.orion.internal_util.ScreenshakeInstance;
import dev.oelohey.orion.util.screenshake.PositionedScreenshakeInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class NbtHelperOrion {
    public static NbtCompound fromScreenshakeInstance(ScreenshakeInstance instance){
        NbtCompound compound = new NbtCompound();
        compound.putInt("duration", instance.duration);
        compound.putInt("maxDuration", instance.maxDuration);
        compound.putFloat("timeMaxPerShake", instance.timeMaxPerShake);
        compound.putFloat("intensityX", instance.intensityX);
        compound.putFloat("intensityY", instance.intensityY);
        compound.putBoolean("diminishIntensity", instance.diminishIntensity);
        if (instance instanceof PositionedScreenshakeInstance positionedScreenshakeInstance){
            NbtCompound origin = new NbtCompound();
            origin.putDouble("X", positionedScreenshakeInstance.origin.x);
            origin.putDouble("Y", positionedScreenshakeInstance.origin.y);
            origin.putDouble("Z", positionedScreenshakeInstance.origin.z);
            compound.put("origin", origin);
            compound.putFloat("maxDistance", positionedScreenshakeInstance.maxDistance);
        }
        return compound;
    }

    public static ScreenshakeInstance toScreenshakeInstance(NbtCompound nbtCompound){
        int duration = nbtCompound.getInt("duration");
        float timeMaxPerShake = nbtCompound.getFloat("timeMaxPerShake");
        float intensityX = nbtCompound.getFloat("intensityX");
        float intensityY = nbtCompound.getFloat("intensityY");
        boolean diminishingIntensity = nbtCompound.getBoolean("diminishIntensity");

        ScreenshakeInstance instance;

        if (nbtCompound.contains("origin")){
            Vec3d origin = new Vec3d(nbtCompound.getCompound("origin").getDouble("X"), nbtCompound.getCompound("origin").getDouble("Y"), nbtCompound.getCompound("origin").getDouble("Z"));
            float maxDistance = nbtCompound.getFloat("maxDistance");
            instance = PositionedScreenshakeInstance.createInstance(duration, timeMaxPerShake, intensityX, intensityY, diminishingIntensity, origin, maxDistance);
        }else {
            instance = GlobalScreenshakeInstance.createInstance(duration, timeMaxPerShake, intensityX, intensityY, diminishingIntensity);
        }
        instance.maxDuration = nbtCompound.getInt("maxDuration");
        return instance;
    }
}
