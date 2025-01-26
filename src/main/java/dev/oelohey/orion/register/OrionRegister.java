package dev.oelohey.orion.register;

import dev.oelohey.orion.handler.ScreenshakeHandler;
import dev.oelohey.orion.handler.SubmersionTypeDataHandler;

public class OrionRegister {

    public static void registerModData(){
        OrionItemComponents.registerItemComponents();

        SubmersionTypeDataHandler.registerHandler();
        ScreenshakeHandler.registerHandler();

        OrionItems.registerItems();
    }
}
