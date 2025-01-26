package dev.oelohey.orion.util;

import dev.oelohey.orion.accesor.BipedEntityRenderStateDoublehandedAcessor;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.LivingEntity;

public class UpdateBipedRenderStateUtilOrion {
    public static void updateBipedRenderStateDoubleHanded(LivingEntity entity, BipedEntityRenderState state, float tickDelta, ItemModelManager itemModelResolver, boolean doubleHanded){
        if (state instanceof BipedEntityRenderStateDoublehandedAcessor acessor){
            acessor.orion$setdoublehanded(doubleHanded);
        }
        BipedEntityRenderer.updateBipedRenderState(entity, state, tickDelta, itemModelResolver);
    }
}
