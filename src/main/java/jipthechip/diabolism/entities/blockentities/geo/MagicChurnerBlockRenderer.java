package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.AbstractFluidContainerRenderer;
import jipthechip.diabolism.entities.blockentities.MagicChurner;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class MagicChurnerBlockRenderer extends AbstractFluidContainerRenderer<MagicChurner> {



    private final Box[] FLUID_BOXES = new Box[]{new Box(3, 2, 3, 13, 3, 13), new Box(2, 3, 2, 14, 4, 14), new Box(1, 4, 1, 15, 11.25,15)};

    public MagicChurnerBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new MagicChurnerBlockModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, MagicChurner animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.push();
        renderFluid(FLUID_BOXES, animatable, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.pop();

    }
}
