package dev.oelohey.orion.mixin;

import dev.oelohey.orion.register.OrionRegister;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel orion$use_different_render_items(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (Item item : OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.keySet()) {
            boolean renderModeCheck = false;
            if (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
                renderModeCheck = true;
            }
            if (stack.isOf(item) && renderModeCheck) {
                String namespace = OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.get(item).getNamespace();
                String id = OrionRegister.DIFFERENT_HAND_MODEL_ITEMS.get(item).getPath();
                return ((ItemRendererAcessor) this).orion$getModels().getModelManager().getModel(new ModelIdentifier(Identifier.of(namespace), id));
            }
        }
        return value;
    }
}
