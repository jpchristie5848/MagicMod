package jipthechip.diabolism.mixin;


import jipthechip.diabolism.potion.DiabolismEffects;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> {

    @Inject(method="setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at=@At("HEAD"))
    private void setThighScale(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci){

        if(livingEntity.hasStatusEffect(DiabolismEffects.RIGHT_TO_YOUR_THIGHS)){
            PlayerEntityModel<T> model = (PlayerEntityModel)(Object) this;
            if(livingEntity.getStatusEffect(DiabolismEffects.RIGHT_TO_YOUR_THIGHS).isDurationBelow(1)){

                model.leftLeg.xScale = 1.0f;
                model.leftLeg.zScale = 1.0f;
                model.leftPants.xScale = 1.0f;
                model.leftPants.zScale = 1.0f;

                model.rightLeg.xScale = 1.0f;
                model.rightLeg.zScale = 1.0f;
                model.rightPants.xScale = 1.0f;
                model.rightPants.zScale = 1.0f;
            }else{
                model.leftLeg.xScale = 1.75f;
                model.leftLeg.zScale = 1.75f;
                model.leftPants.xScale = 1.75f;
                model.leftPants.zScale = 1.75f;

                model.rightLeg.xScale = 1.75f;
                model.rightLeg.zScale = 1.75f;
                model.rightPants.xScale = 1.75f;
                model.rightPants.zScale = 1.75f;
            }
        }

    }
}
