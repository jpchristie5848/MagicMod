package jipthechip.diabolism.entities.blockentities.geo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;

public class DynamicGeoBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    Identifier textureOverride;

    public DynamicGeoBlockRenderer(GeoModel<T> model) {
        super(model);
    }

    /** Taken from @DynamicGeoEntityRenderer, with parts taken from @GeoBlockRenderer and @GeoRenderer**
     */
    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        poseStack.push();

        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
            Matrix4f worldState = new Matrix4f(localMatrix);
            BlockPos pos = this.animatable.getPos();

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(localMatrix);
            bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
        }

        RenderUtils.prepMatrixForBone(poseStack, bone);

        this.textureOverride = getTextureOverrideForBone(bone, this.animatable, partialTick);
        Identifier texture = this.textureOverride == null ? getTexture(this.animatable) : this.textureOverride;
        RenderLayer renderTypeOverride = getRenderTypeOverrideForBone(bone, this.animatable, texture, bufferSource, partialTick);

        if (texture != null && renderTypeOverride == null)
            renderTypeOverride = getRenderType(this.animatable, texture, bufferSource, partialTick);

        if (renderTypeOverride != null)
            buffer = bufferSource.getBuffer(renderTypeOverride);

        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        if (renderTypeOverride != null)
            buffer = bufferSource.getBuffer(getRenderType(this.animatable, getTexture(this.animatable), bufferSource, partialTick));

        if (!isReRender)
            applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        super.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        poseStack.pop();

    }

    /** Taken from @DynamicGeoEntityRenderer **
     * For each bone rendered, this method is called.<br>
     * If a ResourceLocation is returned, the renderer will render the bone using that texture instead of the default.<br>
     * This can be useful for custom rendering  on a per-bone basis.<br>
     * There is a somewhat significant performance cost involved in this however, so only use as needed.
     *
     * @return The specified ResourceLocation, or null if no override
     */
    @Nullable
    protected Identifier getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        return null;
    }

    /** Taken from @DynamicGeoEntityRenderer **
     * For each bone rendered, this method is called.<br>
     * If a RenderType is returned, the renderer will render the bone using that RenderType instead of the default.<br>
     * This can be useful for custom rendering operations on a per-bone basis.<br>
     * There is a somewhat significant performance cost involved in this however, so only use as needed.
     * @return The specified RenderType, or null if no override
     */
    @Nullable
    protected RenderLayer getRenderTypeOverrideForBone(GeoBone bone, T animatable, Identifier texturePath, VertexConsumerProvider bufferSource, float partialTick) {
        return null;
    }

    public Identifier getTexture(T animatable) {
        return getTextureLocation(animatable);
    }
}
