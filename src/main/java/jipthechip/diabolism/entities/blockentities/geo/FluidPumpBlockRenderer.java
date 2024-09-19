package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.blocks.FluidPumpBlock;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FluidPumpBlockRenderer extends GeoBlockRenderer<FluidPump> {

    public FluidPumpBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new FluidPumpBlockModel());
        addRenderLayer(new FluidPumpBlockRenderLayer(this));
    }

    @Override
    public void preRender(MatrixStack poseStack, FluidPump animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);


        Direction dir = animatable.getPumpFromDir();
        if(dir != null){
            applyPumpRotation(poseStack, dir, false);
        }


    }

    @Override
    public void actuallyRender(MatrixStack poseStack, FluidPump animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void applyPumpRotation(MatrixStack poseStack, Direction fromDir, boolean reverse){
        Direction.Axis axis = fromDir.getAxis();
        float degrees;
        RotationAxis rotationAxis;
        if(axis == Direction.Axis.X || axis == Direction.Axis.Z){
            degrees = (fromDir.getOpposite().getHorizontal()*-90);
            rotationAxis = RotationAxis.POSITIVE_Y;
        }else{
            degrees = (fromDir.getOpposite().getId()*2*90)+90;
            rotationAxis = RotationAxis.POSITIVE_X;
        }
        if(reverse) degrees *= -1;
        poseStack.multiply(rotationAxis.rotationDegrees(degrees), 0.5f, 0.5f, 0.5f);
    }
}
