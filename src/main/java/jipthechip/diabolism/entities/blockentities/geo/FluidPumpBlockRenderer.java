package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
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
}
