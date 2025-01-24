package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionItemComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {
	@Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
	private void orion$cactus_proof_items(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if (entity instanceof ItemEntity itemEntity) {
            if (itemEntity.getStack().getOrDefault(OrionItemComponents.CACTUS_RESISTANT, false)) {
				ci.cancel();
			}
		}
	}
}