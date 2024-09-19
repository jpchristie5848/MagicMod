package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class AbstractOmniDirectionalBlock extends BlockWithEntity {

    protected int MAX_CONNECTIONS;

    protected static BooleanProperty NORTH = BooleanProperty.of("north");
    protected static BooleanProperty SOUTH = BooleanProperty.of("south");
    protected static BooleanProperty EAST = BooleanProperty.of("east");
    protected static BooleanProperty WEST = BooleanProperty.of("west");
    protected static BooleanProperty UP = BooleanProperty.of("up");
    protected static BooleanProperty DOWN = BooleanProperty.of("down");


    public static HashMap<Direction, BooleanProperty> DIRECTION_PROPERTIES = new HashMap<>(){{
        put(Direction.NORTH, NORTH);
        put(Direction.SOUTH, SOUTH);
        put(Direction.EAST, EAST);
        put(Direction.WEST, WEST);
        put(Direction.UP, UP);
        put(Direction.DOWN, DOWN);
    }};

    protected List<VoxelShape> CENTER_SHAPES;

    protected List<VoxelShape> NORTH_SHAPES;
    protected List<VoxelShape> SOUTH_SHAPES;
    protected List<VoxelShape> EAST_SHAPES;
    protected List<VoxelShape> WEST_SHAPES;
    protected List<VoxelShape> UP_SHAPES;
    protected List<VoxelShape> DOWN_SHAPES;


    public HashMap<Direction, List<VoxelShape>> DIRECTION_SHAPES;

    public AbstractOmniDirectionalBlock(Settings settings) {
        super(settings);

        BlockState defaultState = getStateManager().getDefaultState();
        for(Direction dir : Direction.values()){
            defaultState = defaultState.with(DIRECTION_PROPERTIES.get(dir), false);
        }
        setDefaultState(defaultState);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        if(CENTER_SHAPES != null){
            List<VoxelShape> shapes = new ArrayList<>(CENTER_SHAPES);

            for(Direction dir : Direction.values()){
                if(state.get(DIRECTION_PROPERTIES.get(dir)))
                    shapes.addAll(DIRECTION_SHAPES.get(dir));
            }

            return shapes.stream().reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        checkConnections(world, pos, state, null, null);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        World actualWorld = (World) world;
        checkDisconnects(actualWorld, pos);
    }

    protected void checkConnections(World world, BlockPos pos, BlockState state, @Nullable List<Class<? extends BlockEntity>> BETypesToConnect, @Nullable List<Direction> excludedDirections){

        int connectionCount = 0;

        for (Direction dir : Direction.values()){

            if(excludedDirections != null && excludedDirections.contains(dir)){
                continue;
            }

            if(connectionCount >= MAX_CONNECTIONS){
                break;
            }
            Vec3i vec = dir.getVector();
            BlockPos checkedPos = pos.add(vec);
            BlockState checkedState = world.getBlockState(checkedPos);
            Block checkedBlock = checkedState.getBlock();
            BlockEntity blockEntity = world.getBlockEntity(checkedPos);

            boolean isInstanceOfSameBlock = this.getClass().getSuperclass().isInstance(checkedState.getBlock()); // checks if blocks share the same superclass
                                                                                                                 // need to know this to connect pipes to each other

            boolean hasAvailableConnections = isInstanceOfSameBlock && countConnections(checkedState) < ((AbstractOmniDirectionalBlock)checkedBlock).getMaxConnections();

            if(isInstanceOfSameBlock && hasAvailableConnections){
                world.setBlockState(checkedPos, checkedState.with(DIRECTION_PROPERTIES.get(dir.getOpposite()), true));
                state = state.with(DIRECTION_PROPERTIES.get(dir), true);
                world.setBlockState(pos, state);
            }
            else if(BETypesToConnect != null){
                for(Class<?> BEType : BETypesToConnect){
                    boolean isInstanceOfBlockEntity = (BEType != null && BEType.isInstance(blockEntity)) && !(isInstanceOfSameBlock); // check if the block is the type of block entity
                                                                                                                                      // that we want to connect to, and not another pipe
                    if(isInstanceOfBlockEntity){

                        state = state.with(DIRECTION_PROPERTIES.get(dir), true);
                        world.setBlockState(pos, state);

                        connectionCount++;
                    }
                }
            }
        }
    }

    protected void checkDisconnects(World world, BlockPos pos){
        for(Direction dir : Direction.values()){
            Vec3i vec = dir.getVector();
            BlockPos checkedPos = pos.add(vec);
            BlockState checkedState = world.getBlockState(checkedPos);

            boolean isInstanceOfSameBlock = this.getClass().isInstance(checkedState.getBlock());

            if(isInstanceOfSameBlock){
                world.setBlockState(checkedPos, checkedState.with(DIRECTION_PROPERTIES.get(dir.getOpposite()), false));
            }
        }
    }

    protected int countConnections(BlockState state){
        int connections = 0;
        if(state.getBlock() instanceof AbstractOmniDirectionalBlock){
            for(Direction dir : Direction.values()){
                if(state.get(DIRECTION_PROPERTIES.get(dir))) connections++;
            }
        }
        return connections;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for(Direction dir : Direction.values()){
            builder.add(DIRECTION_PROPERTIES.get(dir));
        }
    }

    protected int getMaxConnections(){
        return MAX_CONNECTIONS;
    }
}
