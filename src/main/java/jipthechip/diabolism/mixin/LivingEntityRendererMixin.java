package jipthechip.diabolism.mixin;

import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.render.CuboidRenderer;
import jipthechip.diabolism.render.RenderDataMappings;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(at=@At(value = "INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;pop()V"), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private <T extends LivingEntity> void renderIceBlock(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        if(livingEntity.hasStatusEffect(DiabolismEffects.MAP.get("frozen"))){

            float height = livingEntity.getHeight();

            float yOffset = 0;
//            if(livingEntity instanceof SlimeEntity){
//                yOffset = -0.25f;
//            }

            Vec3d from = new Vec3d(-0.5, yOffset, -0.5);
            Vec3d to = new Vec3d(from.getX()+1, from.getY()+height+yOffset, from.getZ()+1);
            //System.out.println("from: "+from);
            //System.out.println("to: "+to);
            CuboidRenderer.renderCuboid(RenderDataMappings.Solids.get("ice"), matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getTranslucent()), i, 0,0, from, to, -1);
        }
    }

}
