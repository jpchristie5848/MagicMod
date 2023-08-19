package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.render.CuboidRenderer;
import jipthechip.diabolism.render.FluidRenderData;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CrystalAltarBlockEntityRenderer implements BlockEntityRenderer<CrystalAltarBlockEntity> {
    private final int BASIN_LEVELS = 3;
    private final Vec3d BOTTOM_BASIN_FROM = new Vec3d(4,8,4);

    public CrystalAltarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){
    }
    @Override
    public void render(CrystalAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (entity.getStoredItem() != Items.AIR || entity.getStoredItem() != null) {
            matrices.push();
            // Calculate the current offset in the y value
            double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            // Move the item
            matrices.translate(0.5, 1.25 + offset, 0.5);

            // Rotate the item
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 4));

            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(entity.getStoredItem(), 1), ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, null, 0);

            matrices.pop();
        }
        matrices.push();

        //CuboidRenderer.renderCuboid(Cuboids.BLOOD, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), light, overlay, 0, new Vec3f(2,10,2), new Vec3f(14,11,14));
        renderLiquid(entity, matrices, vertexConsumers, light, overlay);


        matrices.pop();
    }

    private void renderLiquid(CrystalAltarBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        FluidRenderData fluidRenderData = entity.getFluidRenderData();
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState block = world.getBlockState(pos);

        if(fluidRenderData != null){
            int amount = entity.getFluidAmount();
            float scaledAmount = ((float)amount / (float)entity.getCapacity())*BASIN_LEVELS;

            for(int i = 0; i < BASIN_LEVELS && i < scaledAmount; i++){
                Vec3d from = new Vec3d(BOTTOM_BASIN_FROM.x, BOTTOM_BASIN_FROM.y, BOTTOM_BASIN_FROM.z);
                from = from.add(-1*i, i, -1*i);
                Vec3d to = new Vec3d(from.x, from.y, from.z);
                to = to.add(8+(i*2),Math.min(1, scaledAmount-i), 8+(i*2));
                //System.out.println("box: "+new Box(from, to));
                CuboidRenderer.renderCuboidBER(fluidRenderData, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()
                ), light, overlay, 0, from, to, -1);
            }
        }
    }
}
