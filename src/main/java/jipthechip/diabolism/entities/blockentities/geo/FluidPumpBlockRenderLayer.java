package jipthechip.diabolism.entities.blockentities.geo;

import com.mojang.blaze3d.systems.RenderSystem;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FluidPumpBlockRenderLayer extends GeoRenderLayer<FluidPump> {

    public FluidPumpBlockRenderLayer(GeoRenderer<FluidPump> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, FluidPump animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        FluidPipeCenterBlockModel centerModel = new FluidPipeCenterBlockModel();
        Identifier centerModelResource = centerModel.getModelResource(animatable);

        //RenderSystem.setShaderTexture(0, centerModel.getTextureResource(animatable));

        RenderLayer centerPipeRenderLayer = RenderLayer.getEntityCutout(centerModel.getTextureResource(animatable));

        // render pipe center
        getRenderer().reRender(centerModel.getBakedModel(centerModelResource), poseStack, bufferSource, animatable, centerPipeRenderLayer,
                bufferSource.getBuffer(centerPipeRenderLayer), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                1, 1, 1, 1);

        //poseStack.translate(0.5,0,0.5);

        FluidOutputPipeBlockModel outputPipeModel = new FluidOutputPipeBlockModel();
        Identifier pipeModelResource = outputPipeModel.getModelResource(animatable);

        if(pipeModelResource != null){
            // render pipe in direction of output
            //RenderSystem.setShaderTexture(0, outputPipeModel.getTextureResource(animatable));

            RenderLayer outputPipeRenderLayer = RenderLayer.getEntityCutout(outputPipeModel.getTextureResource(animatable));

            getRenderer().reRender(outputPipeModel.getBakedModel(pipeModelResource), poseStack, bufferSource, animatable, outputPipeRenderLayer,
                    bufferSource.getBuffer(outputPipeRenderLayer), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                    1, 1, 1, 1);
        }
    }
}
