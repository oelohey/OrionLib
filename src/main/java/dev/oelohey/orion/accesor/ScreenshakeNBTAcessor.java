package dev.oelohey.orion.accesor;

import dev.oelohey.orion.infrastructure.ScreenshakeInstance;

import java.util.List;

public interface ScreenshakeNBTAcessor {
    List<ScreenshakeInstance> orion$getInstances();
    void orion$addInstance(ScreenshakeInstance instance);
    void orion$resetInstances();
}
