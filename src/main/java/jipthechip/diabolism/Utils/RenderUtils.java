package jipthechip.diabolism.Utils;

import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RenderUtils {

    public static <T extends GeoAnimatable> void renderGeoModel(GeoRenderer<T> renderer, GeoModel<T> model, MatrixStack poseStack, T animatable, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
        Identifier modelResource = model.getModelResource(animatable);
        RenderLayer modelRenderLayer = RenderLayer.getEntityCutout(model.getTextureResource(animatable));
        // render pipe center
        renderer.reRender(model.getBakedModel(modelResource), poseStack, bufferSource, animatable, modelRenderLayer,
                bufferSource.getBuffer(modelRenderLayer), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                1, 1, 1, 1);
    }

    public static <T extends BlockEntity> void renderItemAboveBlockEntity(ItemStack inputStack, Item itemToRender, MatrixStack poseStack, VertexConsumerProvider bufferSource,
                                                                          float partialTick, T blockEntity, @Nullable Supplier<Double> offsetCalculator){

        if(!inputStack.isEmpty() && inputStack.getItem() == itemToRender){

            World world = blockEntity.getWorld();

            if(world != null){

                poseStack.push();
                long time = world.getTime();

                // Calculate the current offset in the y value
                double offset;

                if(offsetCalculator == null){
                    offset = Math.sin((time + partialTick) / 8.0) / 4.0;
                }else{
                    offset = offsetCalculator.get();
                }
                // Move the item
                poseStack.translate(0, 1.5 + offset , 0);

                // Rotate the item
                poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((time + partialTick) * 4));

                int lightAbove = WorldRenderer.getLightmapCoordinates(world, blockEntity.getPos().up());
                MinecraftClient.getInstance().getItemRenderer().renderItem(inputStack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, poseStack, bufferSource, null, 0);

                poseStack.pop();
            }
        }
    }

    @FunctionalInterface
    public interface PartialTickBERCalculation {

        double calculate(long time, float partialTick, BlockEntity blockEntity);

    }

}
