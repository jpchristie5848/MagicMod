package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.entities.blockentities.AbstractMagickaConsumerBlockRenderer;
import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ArcaneAltarBlockRenderer extends AbstractMagickaConsumerBlockRenderer<ArcaneAltar> {

    public ArcaneAltarBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new ArcaneAltarBlockModel());
    }

}
