package dev.oelohey.orion.mixin;

import dev.oelohey.orion.accesor.BipedEntityRenderStateDoublehandedAcessor;
import dev.oelohey.orion.util.CrossbowPosingOrion;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class TwoHandGripPoseFix<T extends BipedEntityRenderState> extends EntityModel<T> implements ModelWithArms, ModelWithHead {

    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightArm;
    @Unique
    private boolean doubleHanded;

    protected TwoHandGripPoseFix(ModelPart root) {
        super(root);
    }

    @Inject(at = @At("RETURN"), method = "positionLeftArm")
    private void orion$fixLeftArm(T state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        if (state instanceof BipedEntityRenderStateDoublehandedAcessor acessor){
            doubleHanded = acessor.orion$doublehanded();
        }
        if (armPose == BipedEntityModel.ArmPose.CROSSBOW_CHARGE){

            if (doubleHanded){
                CrossbowPosingOrion.doublehanded(this.rightArm, this.leftArm, state.crossbowPullTime, state.itemUseTime, true);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "positionRightArm")
    private void orion$fixRightArm(T state, BipedEntityModel.ArmPose armPose, CallbackInfo ci) {
        if (state instanceof BipedEntityRenderStateDoublehandedAcessor acessor){
            doubleHanded = acessor.orion$doublehanded();
        }
        if (armPose == BipedEntityModel.ArmPose.CROSSBOW_CHARGE){
            if (doubleHanded){
                CrossbowPosingOrion.doublehanded(this.rightArm, this.leftArm, state.crossbowPullTime, state.itemUseTime, false);
            }
        }
    }

}