package dev.oelohey.orion;

import dev.oelohey.orion.register.OrionEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.server.world.ServerWorld;

@Environment(EnvType.CLIENT)
public class OrionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(OrionEntities.TEST, EmptyEntityRenderer::new);
    }
}
