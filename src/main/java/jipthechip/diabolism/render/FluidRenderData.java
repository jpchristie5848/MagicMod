package jipthechip.diabolism.render;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.Fluids;

public class FluidRenderData extends CuboidRenderData {

    public FluidRenderData(String name, int color, int luminosity, int opacityOffset, String... texturePaths) {
        super(name, color, luminosity, opacityOffset, texturePaths);
    }


}
