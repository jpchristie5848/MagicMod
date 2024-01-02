package jipthechip.diabolism.client.models.items;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class WizwichModel implements UnbakedModel {

    Transformation leftHandFP = new Transformation(new Vector3f(-90, 0, 0), new Vector3f(0.1f,0.1f,0), new Vector3f(0.75f, 0.75f, 0.75f));
    Transformation rightHandFP = new Transformation(new Vector3f(-90, 0, 0), new Vector3f(0.1f, 0.1f, 0), new Vector3f(0.75f, 0.75f, 0.75f));
    Transformation leftHandTP = new Transformation(new Vector3f(-90, 0, 0), new Vector3f(0,0f,0.075f), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation rightHandTP = new Transformation(new Vector3f(-90, 0, 0), new Vector3f(0, 0f, 0.075f), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation ground = new Transformation(new Vector3f(-90, 0, 0), new Vector3f(0, 0.125f, 0), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation fixed = new Transformation(new Vector3f(0, 180, 0), new Vector3f(0, 0, 0), new Vector3f(0.75f, 0.75f, 0.75f));


    private final ModelTransformation transformation = new ModelTransformation(leftHandTP, rightHandTP, leftHandFP, rightHandFP, Transformation.IDENTITY, Transformation.IDENTITY, ground, fixed);

    private final SpriteIdentifier particleSprite = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/bread"));

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Nullable
    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return new ItemLayerModel(new WizwichTextureProvider(), transformation, particleSprite);
    }
}
