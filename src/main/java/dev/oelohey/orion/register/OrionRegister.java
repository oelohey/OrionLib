package dev.oelohey.orion.register;

import dev.oelohey.orion.handler.ScreenshakeHandler;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class OrionRegister {
    public static final Map<Item, Identifier> DIFFERENT_HAND_MODEL_ITEMS = new HashMap<>();

    public static void registerModData(){
        OrionItemComponents.registerItemComponents();

        SubmersionTypeDataHandler.registerHandler();
        ScreenshakeHandler.registerHandler();

        OrionItems.registerItems();

    }
}
