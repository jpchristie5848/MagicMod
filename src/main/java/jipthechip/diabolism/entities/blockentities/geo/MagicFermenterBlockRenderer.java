package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.AbstractFluidContainerRenderer;
import jipthechip.diabolism.entities.blockentities.MagicFermenter;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class MagicFermenterBlockRenderer extends AbstractFluidContainerRenderer<MagicFermenter> {

    private final Box[] FLUID_BOXES = new Box[]{new Box(1,1,6,15,13,10), new Box(2,1,10,14,13,13), new Box(3,1,13, 13,13,14),
            new Box(6,1,14,10,13,15), new Box(2,1,3,14,13,6), new Box(3,1,2,13,13,3),  new Box(6,1,1,10,13,2)};

    public MagicFermenterBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new MagicFermenterBlockModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, MagicFermenter animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.push();
        renderFluid(FLUID_BOXES, animatable, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.pop();

    }
}
