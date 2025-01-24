package dev.oelohey.orion;

import dev.oelohey.orion.listeners.DoubleHandedCancel;
import dev.oelohey.orion.register.OrionRegister;
import net.fabricmc.api.ModInitializer;

import net.minecraft.client.render.Camera;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrionLib implements ModInitializer {
	public static final String MOD_ID = "orion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		OrionRegister.registerModData();

		//CALLBACKS
		DoubleHandedCancel.cancelCallbacks();
	}
}