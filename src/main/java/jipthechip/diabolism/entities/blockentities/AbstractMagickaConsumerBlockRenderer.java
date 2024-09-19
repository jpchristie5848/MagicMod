package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.entities.blockentities.geo.DynamicGeoBlockRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AbstractMagickaConsumerBlockRenderer<T extends AbstractMagickaConsumer> extends DynamicGeoBlockRenderer<T> {

    public AbstractMagickaConsumerBlockRenderer(GeoModel<T> model) {
        super(model);
    }

}
