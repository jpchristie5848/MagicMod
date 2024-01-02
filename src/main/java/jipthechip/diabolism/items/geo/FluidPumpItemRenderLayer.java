package jipthechip.diabolism.items.geo;

import jipthechip.diabolism.entities.blockentities.geo.FluidPipeCenterBlockModel;
import jipthechip.diabolism.items.FluidPumpBlockItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FluidPumpItemRenderLayer extends GeoRenderLayer<FluidPumpBlockItem> {

    public FluidPumpItemRenderLayer(GeoRenderer<FluidPumpBlockItem> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, FluidPumpBlockItem animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        // TODO not rendering in correct place due to display settings/transformations, maybe will fix at some point

//        poseStack.translate(0,-1,0);
//        FluidPipeCenterItemModel centerModel = new FluidPipeCenterItemModel();
//        Identifier centerModelResource = centerModel.getModelResource(animatable);
//
//        //RenderSystem.setShaderTexture(0, centerModel.getTextureResource(animatable));
//
//        RenderLayer centerPipeRenderLayer = RenderLayer.getEntityCutout(centerModel.getTextureResource(animatable));
//
//        // render pipe center
//        getRenderer().reRender(centerModel.getBakedModel(centerModelResource), poseStack, bufferSource, animatable, centerPipeRenderLayer,
//                bufferSource.getBuffer(centerPipeRenderLayer), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
//                1, 1, 1, 1);
    }
}
