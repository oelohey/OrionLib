package dev.oelohey.orion.data_types;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface CustomSubmersionType {
    /**
     * Extend this class for creating a custom submersion type
     * then instantiate and add the new instance to the list
     * customSubmersionTypes in
     * @see dev.oelohey.orion.handler.SubmersionTypeDataHandler
     */
    String getSubmersionTypeName();

    float getRedValue();
    float getGreenValue();
    float getBlueValue();
    //Survival and Creative Values
    float getFogStart();
    float getFogEnd();

    boolean multipliesViewDistanceFogStart();
    boolean multipliesViewDistanceFogEnd();
    //Spectator Values
    float getFogStartSpectator();
    float getFogEndSpectator();

    boolean multipliesViewDistanceFogStartSpectator();
    boolean multipliesViewDistanceFogEndSpectator();

    boolean renderSky();

    Block blockForApply();
    boolean passCondition(BlockState blockState, BlockPos blockPos, Vec3d vec3d);
}
