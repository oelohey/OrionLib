package dev.oelohey.orion.register;

import com.mojang.serialization.Codec;
import dev.oelohey.orion.OrionLib;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class OrionItemComponents {

    public static final ComponentType<Boolean> DOUBLE_HANDED = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(OrionLib.MOD_ID, "double_handed"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static final ComponentType<Boolean> CACTUS_RESISTANT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(OrionLib.MOD_ID, "cactus_resistant"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static void registerItemComponents(){

    }
}
