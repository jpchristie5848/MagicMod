package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.render.CuboidRenderer;
import jipthechip.diabolism.render.FluidRenderData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class AbstractGeoBlockWithFluidRenderer<T extends AbstractFluidContainer> extends GeoBlockRenderer<T> {


    public AbstractGeoBlockWithFluidRenderer(GeoModel<T> model) {
        super(model);
    }

    // renders the fluid portion of the block entity
    // @param fluidBoxes    the boxes that represent the area in which the fluid is contained
    // @param entity        the fluid containing block entity
    // @param matrices
    // @param vertexConsumers
    // @param light
    // @param overlay
    protected void renderFluid(Box[] fluidBoxes, T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        FluidRenderData fluid = entity.getFluidRenderData();
        double minHeight = 16;
        double maxHeight = 0;

        for(Box box : fluidBoxes){
            if(box.minY < minHeight){
                minHeight = box.minY;
            }
            if(box.maxY > maxHeight){
                maxHeight = box.maxY;
            }
        }


        if(fluid != null){
            int amount = entity.getFluidAmount();
            int color = entity.getFluidColor();

//            System.out.println("churner pos: "+entity.getPos());
//            System.out.println("churner amount in render: "+entity.getFluidAmount());
//            System.out.println("churner data in render: "+entity.getFluidColor()+" "+ Arrays.toString(entity.getElementContents()));

            double numLevels = (maxHeight - minHeight);

            float scaledAmount = ((float)amount / (float)entity.getCapacity())*((float)numLevels);


            for (Box fluidBox : fluidBoxes) {
                Vec3d from = new Vec3d(fluidBox.minX, fluidBox.minY, fluidBox.minZ);

                double heightToFill = (Math.min(fluidBox.getYLength(), Math.max(0, scaledAmount - from.getY() + minHeight)));

                Vec3d to = new Vec3d(fluidBox.maxX, fluidBox.minY + heightToFill, fluidBox.maxZ);

                if(heightToFill > 0){
                    CuboidRenderer.renderCuboidBER(fluid, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), light, overlay, 0,from,to, color);
                }
            }
//            System.out.println("filledLevels: "+filledLevels);
//            System.out.println("scaledAmount: "+scaledAmount);
//            System.out.println("numLevels: "+numLevels);
//            System.out.println("amount: "+amount);
        }
    }
}
