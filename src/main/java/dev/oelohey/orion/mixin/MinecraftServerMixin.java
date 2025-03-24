package dev.oelohey.orion.mixin;

import com.mojang.datafixers.DataFixer;
import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.accesor.MinecraftServerAccesor;
import dev.oelohey.orion.infrastructure.wrapper_world.WrapperScheduleInterface;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.ApiServices;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

	@Shadow @Final protected SaveProperties saveProperties;

	@Shadow public abstract DynamicRegistryManager.Immutable getRegistryManager();

	@Shadow @Final protected LevelStorage.Session session;

	@Inject(at = @At("RETURN"), method = "createWorlds")
	private void orion$create_interface(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
		ServerWorldProperties serverWorldProperties = this.saveProperties.getMainWorldProperties();
		OrionLib.INTERFACE = new WrapperScheduleInterface((MinecraftServer)(Object)this, serverWorldProperties, this.getRegistryManager().getOrThrow(RegistryKeys.DIMENSION).get(DimensionOptions.OVERWORLD), session);
	}
}