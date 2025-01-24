package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionRegister;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void addModelToBake(ModelIdentifier id,  UnbakedModel model);
    @Shadow
    protected abstract JsonUnbakedModel loadModelFromJson(Identifier id);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;loadItemModel(Lnet/minecraft/client/util/ModelIdentifier;)V"))
    public void orion$load_different_model_items(BlockColors blockColors, Profiler profiler, Map jsonUnbakedModels, Map blockStates, CallbackInfo ci) {
        profiler.swap("items");
        for (Item item : OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.keySet()) {
            String namespace = OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.get(item).getNamespace();
            String id = OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.get(item).getPath();

            UnbakedModel unbakedModel =this.loadModelFromJson(OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.get(item).withPrefixedPath("item/"));
            this.addModelToBake(new ModelIdentifier(Identifier.of(namespace), id), unbakedModel);
        }
        profiler.swap("special");
    }
}
