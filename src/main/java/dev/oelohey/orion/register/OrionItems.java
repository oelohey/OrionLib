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
    private static final RegistryKey<Item> EARTHQUAKE_STICK_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OrionLib.MOD_ID, "earthquake_stick"));
    public static final Item EARTHQUAKE_STICK = register(new EarthquakeStick(new Item.Settings().registryKey(EARTHQUAKE_STICK_KEY)), EARTHQUAKE_STICK_KEY.getValue());

    public static Item register(Item item, Identifier identifier) {
        // Register the item.

        // Return the registered item!
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void registerItems(){}
}
