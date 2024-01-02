package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.mixin.SpriteAccessor;
import jipthechip.diabolism.mixin.SpriteContentsAccessor;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemLayerModel implements BakedModel, FabricBakedModel {


    private static final Direction[] HORIZONTALS = {Direction.UP, Direction.DOWN};
    private static final Direction[] VERTICALS = {Direction.WEST, Direction.EAST};

    private final ItemLayerTextureProvider textureGetter;
    private final ModelTransformation transformation;

    Transformation default_leftHandFP = new Transformation(new Vector3f(0, -90, 30), new Vector3f(0.1f,0.1f,0), new Vector3f(0.75f, 0.75f, 0.75f));
    Transformation default_rightHandFP = new Transformation(new Vector3f(0, -90, 30), new Vector3f(0.1f, 0.1f, 0), new Vector3f(0.75f, 0.75f, 0.75f));
    Transformation default_leftHandTP = new Transformation(new Vector3f(0, 0, 0), new Vector3f(0,0.2f,0.075f), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation default_rightHandTP = new Transformation(new Vector3f(0, 0, 0), new Vector3f(0, 0.2f, 0.075f), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation default_ground = new Transformation(new Vector3f(0, 0, 0), new Vector3f(0, 0.125f, 0), new Vector3f(0.52f, 0.52f, 0.52f));
    Transformation default_fixed = new Transformation(new Vector3f(0, 180, 0), new Vector3f(0, 0, 0), new Vector3f(0.75f, 0.75f, 0.75f));

    SpriteIdentifier particleSprite;

    private final ModelTransformation DEFAULT_TRANSFORMATION = new ModelTransformation(default_leftHandTP, default_rightHandTP, default_leftHandFP, default_rightHandFP, Transformation.IDENTITY, Transformation.IDENTITY, default_ground, default_fixed);


    public ItemLayerModel(ItemLayerTextureProvider textureGetter, @Nullable ModelTransformation transformation, @Nullable SpriteIdentifier particleSprite){
        this.textureGetter = textureGetter;

        this.transformation = transformation == null ? DEFAULT_TRANSFORMATION : transformation;

        this.particleSprite = particleSprite;
    }


    public static Mesh getQuadsForSprite(int color, int tint, Sprite sprite, Transformation transform, int luminosity, @Nullable ItemLayerPixels pixels, int layerIndex){
        MeshBuilder mesh = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        SpriteContents contents = ((SpriteAccessor)sprite).getSpriteContents();

        int uMax = contents.getWidth();
        int vMax = contents.getHeight();

        FaceData faceData = new FaceData(uMax, vMax);
        NativeImage image = ((SpriteContentsAccessor)contents).getImage();

        boolean translucent = false;

        for(int v = 0; v < vMax; v++){
            boolean t = true;
            boolean t_up;
            boolean t_left;

            for(int u = 0; u < uMax; u++){
                int alpha = (image.getColor(u, v) >> 24 & 0xFF);
                int alpha_up = v == 0 ? 0 : (image.getColor(u, v-1) >> 24 & 0xFF);
                int alpha_left = u == 0 ? 0 : (image.getColor(u-1, v) >> 24 & 0xFF);

                t = alpha / 255.0f <= 0.1f;
                t_up = alpha_up / 255f <= 0.1f;
                t_left = alpha_left / 255f <= 0.1f;

                if(!t && t_left){ // if current is opaque and left isn't, draw left face
                    faceData.set(Direction.WEST, u, v);
                }
                if(!t && t_up){ // if current is opaque and up isn't, draw up face
                    faceData.set(Direction.UP, u, v);
                }
                if(t && !t_left){ // if current is transparent and left isn't, draw right face on left
                    faceData.set(Direction.EAST, u-1, v);
                }
                if(t && !t_up){ // if current is transparent and up isn't, draw down face on up
                    faceData.set(Direction.DOWN, u, v-1);
                }

                if(v == vMax - 1 && !t){ // check if down faces need to be drawn in bottom row
                    faceData.set(Direction.DOWN, u, v);
                }
            }
            if(!t){ // check if right face needs to be drawn for last pixel in row
                faceData.set(Direction.EAST, uMax-1, v);
            }
        }

        int totalNumQuads = 0;

        for(Direction facing : HORIZONTALS){
            for(int v = 0; v < vMax; v++){
                int uStart = 0, uEnd = uMax;
                boolean building = false;
                for(int u = 0; u < uMax; u++){
                    boolean alreadyDrawn = pixels != null && pixels.get(u, v, layerIndex);
                    boolean face = !alreadyDrawn && faceData.get(facing, u, v);

                    if(face){ // check if quad needs to be built at this coordinate
                        uEnd = u+1;
                        if(!building){ // if quad is not already being built, begin building
                            building = true;
                            uStart = u;
                        }
                    }
                    else if(building){
                        int offset = facing == Direction.DOWN ? 1 : 0;
                        buildSideQuad(mesh, transform, facing, color, tint, sprite, uStart, v + offset, uEnd - uStart, luminosity, layerIndex);
                        totalNumQuads += uEnd-uStart;
                        building = false;
                    }
                }
                if(building){ // build quad at end if necessary
                    int offset = facing == Direction.DOWN ? 1 : 0;
                    buildSideQuad(mesh, transform, facing, color, tint, sprite, uStart, v + offset, uEnd - uStart, luminosity, layerIndex);
                    totalNumQuads += uEnd-uStart;
                }
            }
        }

        // TODO build verticals
        for(Direction facing : VERTICALS){
            for(int u = 0; u < uMax; u++){
                int vStart = 0, vEnd = vMax;
                boolean building = false;
                for(int v = 0; v < vMax; v++){
                    boolean alreadyDrawn = pixels != null && pixels.get(u, v, layerIndex);
                    boolean face = !alreadyDrawn && faceData.get(facing, u, v);

                    if(face){ // check if quad needs to be built at this coordinate
                        vEnd = v+1;
                        if(!building){ // if quad is not already being built, begin building
                            building = true;
                            vStart = v;
                        }
                    }
                    else if(building){
                        int offset = facing == Direction.EAST ? 1 : 0;
                        buildSideQuad(mesh, transform, facing, color, tint, sprite, u + offset, vStart, vEnd - vStart, luminosity, layerIndex);
                        totalNumQuads += vEnd - vStart;
                        building = false;
                    }
                }
                if(building){ // build quad at end if necessary
                    int offset = facing == Direction.EAST ? 1 : 0;
                    //offset = 0;
                    buildSideQuad(mesh, transform, facing, color, tint, sprite, u + offset, vStart, vEnd - vStart, luminosity, layerIndex);
                    totalNumQuads += vEnd - vStart;
                }
            }
        }

        float zFront = (7.5f + layerIndex) / 16;
        float zBack = (8.5f + layerIndex) / 16;

        // front
        buildQuad(mesh, transform, Direction.NORTH, sprite, color, tint, luminosity,
                0, 0, zFront, sprite.getMinU(), sprite.getMaxV(),
                0, 1, zFront, sprite.getMinU(), sprite.getMinV(),
                1, 1, zFront, sprite.getMaxU(), sprite.getMinV(),
                1, 0, zFront, sprite.getMaxU(), sprite.getMaxV()
        );
        // back
        buildQuad(mesh, transform, Direction.SOUTH, sprite, color, tint, luminosity,
                0, 0, zBack, sprite.getMinU(), sprite.getMaxV(),
                1, 0, zBack, sprite.getMaxU(), sprite.getMaxV(),
                1, 1, zBack, sprite.getMaxU(), sprite.getMinV(),
                0, 1, zBack, sprite.getMinU(), sprite.getMinV()
        );


        // fill in the pixel map with new pixels from the sprite
        if (pixels != null) {
            for(int v = 0; v < vMax; v++) {
                for(int u = 0; u < uMax; u++) {
                    int alpha = image.getColor(u, v) >> 24 & 0xFF;
                    if (alpha / 255f > 0.1f) {
                        pixels.set(u, v, layerIndex);
                    }
                }
            }

        }

        return mesh.build();
    }


    private static void buildSideQuad(MeshBuilder builder, Transformation transform, Direction side, int color, int tint, Sprite sprite, int u, int v, int size, int luminosity, int layerIndex){

        SpriteContents spriteContents = ((SpriteAccessor)sprite).getSpriteContents();
        NativeImage image = ((SpriteContentsAccessor)spriteContents).getImage();

        int width = image.getWidth();
        int height = image.getHeight();

        float x0 = (float) u / width; // starting x
        float y0 = (float) (height - v) / height; // starting y
        float x1 = x0, y1 = y0; // ending x and y

        float zHigh = (8.5f + layerIndex) / 16;
        float zLow = (7.5f + layerIndex) / 16;

        float z0 = zHigh;
        float z1 = zLow;

        switch(side) {
            case WEST:
                z0 = zLow;
                z1 = zHigh;
                // continue into EAST
            case EAST:
                y1 = (float) ((height - v) - size) / height; // y is starting y + size
                break;
            case DOWN:
                z0 = zLow;
                z1 = zHigh;
                // continue into UP
            case UP:
                x1 = (float) (u + size) / width; // x is starting x + size
                break;
            default:
                throw new IllegalArgumentException("can't handle z-oriented side");
        }

        //float zDiff = Math.abs(z0 - z1);

        float uOff0 = 0;
        float vOff0 = 0;
        float uOff1 = 0;
        float vOff1 = 0;

        if(x0 == x1){
            if(side == Direction.WEST)
                uOff1 = (float)1/width;
            else if(side == Direction.EAST)
                uOff0 = (float)-1/width;
        }else if(y0 == y1){
            if(side == Direction.DOWN)
                vOff1 = (float)1/height;
            else if(side == Direction.UP)
                vOff0 = (float)-1/height;
        }

        float u0 = (x0+uOff0)*(sprite.getMaxU() - sprite.getMinU()) + sprite.getMinU(); // left side texture
        float u1 = (x1+uOff1)*(sprite.getMaxU() - sprite.getMinU()) + sprite.getMinU(); // right side texture
        float v0 = (1-y0-vOff0)*(sprite.getMaxV() - sprite.getMinV()) + sprite.getMinV(); // top side texture
        float v1 = (1-y1-vOff1)*(sprite.getMaxV() - sprite.getMinV()) + sprite.getMinV(); // bottom side texture

        if(side == Direction.UP || side == Direction.DOWN){ // rotate uv for up/down faces
            buildQuad(
                    builder, transform, side,
                    sprite, color, tint, luminosity,
                    x0, y0, z0, u0, v1, // top left
                    x1, y1, z0, u1, v1, // bottom left
                    x1, y1, z1, u1, v0, // bottom right
                    x0, y0, z1, u0, v0); // top right
        }else{
            buildQuad(
                    builder, transform, side,
                    sprite, color, tint, luminosity,
                    x0, y0, z0, u0, v0, // top left
                    x1, y1, z0, u0, v1, // bottom left
                    x1, y1, z1, u1, v1, // bottom right
                    x0, y0, z1, u1, v0); // top right
        }

    }

    protected static void buildQuad(MeshBuilder builder, Transformation transform, Direction side, Sprite sprite, int color, int tint, int luminosity,
                                    float x0, float y0, float z0, float u0, float v0,
                                    float x1, float y1, float z1, float u1, float v1,
                                    float x2, float y2, float z2, float u2, float v2,
                                    float x3, float y3, float z3, float u3, float v3) {
        QuadEmitter emitter = builder.getEmitter();
        MaterialFinder material = RendererAccess.INSTANCE.getRenderer().materialFinder();
        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_ROTATE_NONE);
        emitter.colorIndex(tint);
        emitter.nominalFace(side);
        emitter.material(material.disableDiffuse(0,true).find());

        putVertex(emitter, side, x0, y0, z0, u0, v0, color, luminosity, 0);
        putVertex(emitter, side, x1, y1, z1, u1, v1, color, luminosity, 1);
        putVertex(emitter, side, x2, y2, z2, u2, v2, color, luminosity, 2);
        putVertex(emitter, side, x3, y3, z3, u3, v3, color, luminosity, 3);

        //QuadTransformers.applying(transform).transform(emitter); // TODO don't have to worry about this part since transform is always IDENTITY?

        emitter.emit();
    }

    private static void putVertex(QuadEmitter consumer, Direction side, float x, float y, float z, float u, float v, int color, int luminosity, int vertexIndex) {

        consumer.pos(vertexIndex, x, y, z);

        consumer.spriteColor(vertexIndex, 0, color);

        consumer.sprite(vertexIndex, 0, u, v);

        consumer.lightmap(vertexIndex, LightmapTextureManager.pack(luminosity, luminosity));

        float offX = (float) side.getOffsetX();
        float offY = (float) side.getOffsetY();
        float offZ = (float) side.getOffsetZ();
        consumer.normal(vertexIndex, offX, offY, offZ);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {

        List<ItemLayerTexture> textures = textureGetter.getTextures(stack); // get the textures and colors for each layer

        Consumer<Mesh> meshConsumer = context.meshConsumer();

        SpriteContents firstContents = ((SpriteAccessor)textures.get(0).sprite()).getSpriteContents();
        NativeImage image = ((SpriteContentsAccessor)firstContents).getImage();

        int width = image.getWidth(), height = image.getHeight();

        ItemLayerPixels pixels = new ItemLayerPixels(width, height); // keeps track of which quads are already being
                                                                     // rendered to prevent z-fighting

        for(int i = 0; i < textures.size(); i++){ // get meshes for each layer of the item (in reverse order)

            ItemLayerTexture texture = textures.get(i);

            Sprite sprite = texture.sprite();
            int color = texture.color();
            int layerIndex = texture.layerIndex();

            Mesh mesh = getQuadsForSprite(color, -1, sprite, Transformation.IDENTITY, 0, pixels, layerIndex);

            meshConsumer.accept(mesh); // render the mesh
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return particleSprite.getSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return this.transformation;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }

    private static class FaceData {
        private final EnumMap<Direction, BitSet> data = new EnumMap<>(Direction.class);
        private final int vMax;

        public FaceData(int uMax, int vMax){

            this.vMax = vMax;

            data.put(Direction.WEST, new BitSet(uMax * vMax));
            data.put(Direction.EAST, new BitSet(uMax * vMax));
            data.put(Direction.UP,   new BitSet(uMax * vMax));
            data.put(Direction.DOWN, new BitSet(uMax * vMax));
        }

        public void set(Direction facing, int u, int v) {
            data.get(facing).set(getIndex(u, v));
        }

        public boolean get(Direction facing, int u, int v) {
            return data.get(facing).get(getIndex(u, v));
        }

        private int getIndex(int u, int v) {
            return v * vMax + u;
        }
    }

    private static class ItemLayerPixels {

        private List<List<BitSet>> layers = new ArrayList<>();
        private int width;
        private int height;

        public ItemLayerPixels(int width, int height){
            this.width = width;
            this.height = height;
            List<BitSet> rows = new ArrayList<>();
            for(int r = 0; r < height; r++){
                rows.add(new BitSet(width));
            }
            layers.add(rows);
        }

        public boolean get(int x, int y, int layerIndex) {
            // if we have no data, obviously not here
            if(layerIndex > layers.size()-1 || x >= width || y >= height) return false;

            List<BitSet> rows = layers.get(layerIndex);

            if (y <= rows.size()) {
                BitSet set = rows.get(y);
                if (set != null) {
                    return set.get(x);
                }
            }
            return false;
        }

        public void set(int x, int y, int layerIndex){
            if(x >= width || y >= height) return;

            if(layerIndex > layers.size()-1){ // create new 2d BitSet if layer doesn't exist
                for(int i = layers.size(); i <= layerIndex; i++){
                    List<BitSet> rows = new ArrayList<>();
                    for(int r = 0; r < height; r++){
                        rows.add(new BitSet(width));
                    }
                    this.layers.add(rows);
                }
            }

            List<BitSet> rows = layers.get(layerIndex);
            rows.get(y).set(x);
        }
    }

}
