package jipthechip.diabolism.entities.blockentities.geo;

import com.mojang.blaze3d.systems.RenderSystem;
import jipthechip.diabolism.Utils.RenderUtils;
import jipthechip.diabolism.blocks.AbstractOmniDirectionalBlock;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FluidPumpBlockRenderLayer extends GeoRenderLayer<FluidPump> {

    public FluidPumpBlockRenderLayer(GeoRenderer<FluidPump> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, FluidPump animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        FluidPumpBlockRenderer renderer = (FluidPumpBlockRenderer) getRenderer();

        Direction pumpFromDir = animatable.getPumpFromDir();

        // undo pump rotation that was done during pre render so subsequent rotations aren't affected
        renderer.applyPumpRotation(poseStack, pumpFromDir, true);

        // render fluid pipe center model
        RenderUtils.renderGeoModel(renderer, new FluidPipeCenterBlockModel(), poseStack, animatable, bufferSource, partialTick, packedLight);

        Direction connectedDir = getConnectedDirection(animatable);

        if(connectedDir != null){
            // rotate model of output pipe so it faces connected block
            applyHorizontalRotation(pumpFromDir.getOpposite(), connectedDir, poseStack);
            applyVerticalRotation(pumpFromDir.getOpposite(), connectedDir, poseStack);

            RenderUtils.renderGeoModel(renderer, new FluidOutputPipeBlockModel(), poseStack, animatable, bufferSource, partialTick, packedLight);
        }
    }

    private Direction getConnectedDirection(FluidPump pump){

        Direction dir = null;

        World world = pump.getWorld();
        if(world != null){
            BlockState state = world.getBlockState(pump.getPos());
            if(state.getBlock() == DiabolismBlocks.FLUID_PUMP){
                for(Direction dir2 : Direction.values()){
                    if(state.get(AbstractOmniDirectionalBlock.DIRECTION_PROPERTIES.get(dir2))){
                        dir = dir2;
                        break;
                    }
                }
            }
        }
        return dir;
    }

    private void applyHorizontalRotation(Direction start, Direction end, MatrixStack poseStack){

        RotationAxis horizontalRotAxis = RotationAxis.NEGATIVE_Y;
        int horizontalRotDegrees = getHorizontalDegreesBetween(start, end);

        // Not sure why I have to do these weird ass offsets
        int xOff = start == Direction.NORTH || start == Direction.WEST ? 1 : 0;
        int zOff = start == Direction.NORTH || start == Direction.EAST ? 1 : 0;

        poseStack.multiply(horizontalRotAxis.rotationDegrees(horizontalRotDegrees), xOff, 0, zOff);
    }

    private int getHorizontalDegreesBetween(Direction start, Direction end){
        if(start == end || !(start.getAxis().isHorizontal() && end.getAxis().isHorizontal())) return 0;
        if(start == end.getOpposite()) return 180;

        int startHorizontal = start.getHorizontal();
        int endHorizontal = end.getHorizontal();

        if(((startHorizontal + 1) % 4) == endHorizontal) return 90;
        else return -90;
    }

    private void applyVerticalRotation(Direction start, Direction end, MatrixStack poseStack){
        if(start == end || (start.getAxis().isHorizontal() && end.getAxis().isHorizontal())) return;

        Vec3d startVec = Vec3d.of(start.getVector());
        Vec3d endVec = Vec3d.of(end.getVector());
        Vec3d diff = endVec.subtract(startVec);

        RotationAxis verticalRotAxis;

        float rotationDegrees;

        if(start == end.getOpposite()){
            verticalRotAxis = RotationAxis.POSITIVE_X; // rotation axis doesn't matter here
            rotationDegrees = 180;
        }
        else {
            Vec3d rotVec;
            if(diff.getZ() != 0){
                verticalRotAxis = RotationAxis.POSITIVE_X;
                rotVec = new Vec3d(1,0,0);

            }else{
                verticalRotAxis = RotationAxis.POSITIVE_Z;
                rotVec = new Vec3d(0,0,1);
            }
            rotationDegrees = startVec.crossProduct(endVec).dotProduct(rotVec) > 0 ? 90 : -90;
        }

        // not sure why I have to do these weird ass offsets
        int xOff = start == Direction.WEST ? 1 : 0;
        int zOff = start == Direction.NORTH ? 1 : 0;

        poseStack.multiply(verticalRotAxis.rotationDegrees(rotationDegrees), xOff, 0.5f, zOff);

    }
}
