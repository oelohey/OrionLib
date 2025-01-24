package dev.oelohey.orion.register;

import dev.oelohey.orion.OrionLib;
import dev.oelohey.orion.item.EarthquakeStick;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class OrionItems {
    public static final Item EARTHQUAKE_STICK = register(new EarthquakeStick(new Item.Settings()), Identifier.of(OrionLib.MOD_ID, "earthquake_stick"));

    public static Item register(Item item, Identifier identifier) {
        return Registry.register(Registries.ITEM, identifier, item);
    }
    public static void registerItems(){}
}
