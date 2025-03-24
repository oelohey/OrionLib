package dev.oelohey.orion.accesor;

import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;

import java.util.concurrent.Executor;

public interface MinecraftServerAccesor {
    LevelStorage.Session getSession();
    Executor getWorkerExecutor();
}
