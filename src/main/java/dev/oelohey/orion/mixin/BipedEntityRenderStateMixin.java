package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.BipedEntityRenderStateDoublehandedAcessor;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BipedEntityRenderState.class)
public abstract class BipedEntityRenderStateMixin implements BipedEntityRenderStateDoublehandedAcessor {

	@Unique
	boolean doubleHanded = false;

	@Override
	public boolean orion$doublehanded(){
		return doubleHanded;
	}

	@Override
	public void orion$setdoublehanded(boolean value){
		doubleHanded = value;
	}
}