package dev.oelohey.orion.internal_util;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.MathHelper;

public class CrossbowPosingOrion {
    public static void doublehanded(ModelPart holdingArm, ModelPart pullingArm, float f, int i, boolean bl) {
        ModelPart modelPart = bl ? holdingArm : pullingArm;
        ModelPart modelPart2 = bl ? pullingArm : holdingArm;
        modelPart.yaw = bl ? -0.3F : 0.4F;
        modelPart.pitch = -0.97079635F;
        modelPart2.pitch = modelPart.pitch;
        float g = MathHelper.clamp((float)i, 0.0F, f);
        float h = g / f;
        modelPart2.yaw = MathHelper.lerp(h, 1.0F, 1.1F) * (float) (bl ? 1 : -1);
        modelPart2.pitch = MathHelper.lerp(h, modelPart2.pitch, (float) (-Math.PI / 2));
    }
}
