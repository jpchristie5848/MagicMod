package jipthechip.diabolism.blocks;


import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import jipthechip.diabolism.entities.blockentities.AbstractMagickaConsumer;
import jipthechip.diabolism.entities.blockentities.FluidPipe;
import jipthechip.diabolism.entities.blockentities.FluidPump;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FluidPumpBlock extends FluidPipeBlock {


    long lastToggled = -1;

    public static DirectionProperty PUMP_FROM_DIRECTION = DirectionProperty.of("pump_from_direction");

    public FluidPumpBlock(Settings settings) {
        super(settings);
        MAX_CONNECTIONS = 1;
        BlockState defaultState = getStateManager().getDefaultState();
        for(Direction dir : Direction.values()){
            defaultState = defaultState.with(DIRECTION_PROPERTIES.get(dir), false);
        }
        setDefaultState(defaultState.with(PUMP_FROM_DIRECTION, Direction.NORTH));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        Direction finalPumpDir = null;
        Direction adjacentContainerPumpDir = null;
        int numAdjacentContainers = 0;
        for(Direction dir : Direction.values()){ // search for an adjacent fluid container to pump from
            Vec3i vec = dir.getVector();
            BlockEntity blockEntity = world.getBlockEntity(pos.add(vec));
            if(blockEntity instanceof AbstractFluidContainer && ! (blockEntity instanceof FluidPump || blockEntity instanceof FluidPipe)){
                numAdjacentContainers++;
                adjacentContainerPumpDir = dir;
            }
        }
        if(numAdjacentContainers == 1){
            finalPumpDir = adjacentContainerPumpDir;
        }
        if(finalPumpDir == null){
            if(placer != null){ // if didn't find a nearby fluid container, just make it pump from the direction the placer is looking
                float pitch = placer.getPitch();
                float yaw = placer.getYaw();
                System.out.println("pitch when placing pump: "+pitch);
                System.out.println("yaw when placing pump: "+yaw);

                if(pitch >= 50){
                    finalPumpDir = Direction.DOWN;
                }else if(pitch <= -50){
                    finalPumpDir = Direction.UP;
                }
                // yaw degrees:
                // north 180/-180
                // south 0
                // east -90
                // west 90
                else if(yaw > -45 && yaw <= 45){
                    finalPumpDir = Direction.SOUTH;
                }else if(yaw > -135 && yaw <= -45){
                    finalPumpDir = Direction.EAST;
                }else if(yaw > 135 || yaw <= -135){
                    finalPumpDir = Direction.NORTH;
                }else {
                    finalPumpDir = Direction.WEST;
                }
            }else{ // if all else fails, default to north
                finalPumpDir = Direction.NORTH;
            }
        }
        state = state.with(PUMP_FROM_DIRECTION, finalPumpDir);
        world.setBlockState(pos, state);
        checkConnections(world, pos, state, List.of(AbstractFluidContainer.class, AbstractMagickaConsumer.class), List.of(finalPumpDir));

    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        List<VoxelShape> shapes = new ArrayList<>();

        shapes.add(super.getOutlineShape(state, world, pos, context));
        shapes.addAll(DIRECTION_SHAPES.get(state.get(PUMP_FROM_DIRECTION)));

        return shapes.stream().reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PUMP_FROM_DIRECTION);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof FluidPump pump && System.currentTimeMillis() - lastToggled > 2000){
            if(pump.toggleActive())
                lastToggled = System.currentTimeMillis();
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPump(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, DiabolismEntities.FLUID_PUMP_BLOCKENTITY, FluidPump::ticker);
    }
}
