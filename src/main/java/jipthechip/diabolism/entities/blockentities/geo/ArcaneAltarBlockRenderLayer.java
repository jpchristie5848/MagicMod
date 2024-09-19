package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.Utils.RenderUtils;
import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.FastBoneFilterGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ArcaneAltarBlockRenderLayer extends GeoRenderLayer<ArcaneAltar> {

    public ArcaneAltarBlockRenderLayer(GeoRenderer<ArcaneAltar> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, ArcaneAltar altar, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        ItemStack stack = altar.getStack(0);

        RenderUtils.renderItemAboveBlockEntity(stack, DiabolismItems.MAGICKA_CRYSTAL, poseStack, bufferSource, partialTick, altar, ()->{
            World world = altar.getWorld();
            if(world != null){
                long time = world.getTime();
                return (Math.sin((time + partialTick) / 8.0) / 8.0) - ((double) altar.getProgress()/100) + 0.75f;
            }
            return 0.0;
        });
    }
}
