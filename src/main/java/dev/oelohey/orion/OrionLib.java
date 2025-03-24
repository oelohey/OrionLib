package dev.oelohey.orion;

import dev.oelohey.orion.command.CommandRegistry;
import dev.oelohey.orion.infrastructure.wrapper_world.WrapperScheduleInterface;
import dev.oelohey.orion.listeners.DoubleHandedCancel;
import dev.oelohey.orion.register.OrionRegister;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrionLib implements ModInitializer {
	public static final String MOD_ID = "orion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static WrapperScheduleInterface INTERFACE;

	@Override
	public void onInitialize() {
		OrionRegister.registerModData();

		//CALLBACKS
		DoubleHandedCancel.cancelCallbacks();

		CommandRegistry.commandRegistry();

	}

	public static Identifier of(String string){
		return Identifier.of(MOD_ID, string);
	}
}