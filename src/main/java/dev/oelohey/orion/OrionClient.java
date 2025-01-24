package dev.oelohey.orion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class OrionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    }
}
