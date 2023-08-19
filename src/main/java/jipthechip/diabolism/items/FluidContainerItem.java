package jipthechip.diabolism.items;

import jipthechip.diabolism.render.FluidRenderData;
import net.minecraft.item.Item;

public abstract class FluidContainerItem extends Item {

    private int fluidColor;
    private int capacity;
    private FluidRenderData fluid;

    public FluidContainerItem(Settings settings, int fluidColor, int capacity, FluidRenderData fluid) {
        super(settings);
        this.fluidColor = fluidColor;
        this.capacity = capacity;
        this.fluid = fluid;
    }


}
