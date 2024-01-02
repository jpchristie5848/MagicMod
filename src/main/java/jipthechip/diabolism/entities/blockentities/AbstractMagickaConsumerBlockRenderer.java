package jipthechip.diabolism.entities.blockentities;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AbstractMagickaConsumerBlockRenderer<T extends AbstractMagickaConsumer> extends GeoBlockRenderer<T> {

    public AbstractMagickaConsumerBlockRenderer(GeoModel<T> model) {
        super(model);
    }
}
