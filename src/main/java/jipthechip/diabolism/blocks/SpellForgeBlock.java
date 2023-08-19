package jipthechip.diabolism.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SpellForgeBlock extends BlockWithEntity {


    DirectionProperty DIRECTION = DirectionProperty.of("direction");

    protected SpellForgeBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(DIRECTION, Direction.NORTH));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        return switch (state.get(DIRECTION)){
            case NORTH -> Stream.of(
                    Block.createCuboidShape(3, 4, 2, 13, 12, 3),
                    Block.createCuboidShape(0, 0, 0, 2, 4, 2),
                    Block.createCuboidShape(14, 0, 0, 16, 4, 2),
                    Block.createCuboidShape(14, 0, 14, 16, 4, 16),
                    Block.createCuboidShape(0, 0, 14, 2, 4, 16),
                    Block.createCuboidShape(13, 4, 13, 16, 12, 16),
                    Block.createCuboidShape(13, 4, 0, 16, 12, 3),
                    Block.createCuboidShape(0, 4, 0, 3, 12, 3),
                    Block.createCuboidShape(0, 4, 13, 3, 12, 16),
                    Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                    Block.createCuboidShape(2, 4, 3, 3, 12, 13),
                    Block.createCuboidShape(13, 4, 3, 14, 12, 13),
                    Block.createCuboidShape(3, 4, 13, 13, 12, 14),
                    Block.createCuboidShape(4, 7, 14, 5, 9, 16),
                    Block.createCuboidShape(7, 7, 14, 8, 9, 16),
                    Block.createCuboidShape(8, 7, 14, 9, 9, 16),
                    Block.createCuboidShape(11, 7, 14, 12, 9, 16),
                    Block.createCuboidShape(9, 7, 15, 11, 9, 16),
                    Block.createCuboidShape(5, 7, 15, 7, 9, 16)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            case SOUTH -> Stream.of(
                    Block.createCuboidShape(3, 4, 13, 13, 12, 14),
                    Block.createCuboidShape(14, 0, 14, 16, 4, 16),
                    Block.createCuboidShape(0, 0, 14, 2, 4, 16),
                    Block.createCuboidShape(0, 0, 0, 2, 4, 2),
                    Block.createCuboidShape(14, 0, 0, 16, 4, 2),
                    Block.createCuboidShape(0, 4, 0, 3, 12, 3),
                    Block.createCuboidShape(0, 4, 13, 3, 12, 16),
                    Block.createCuboidShape(13, 4, 13, 16, 12, 16),
                    Block.createCuboidShape(13, 4, 0, 16, 12, 3),
                    Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                    Block.createCuboidShape(13, 4, 3, 14, 12, 13),
                    Block.createCuboidShape(2, 4, 3, 3, 12, 13),
                    Block.createCuboidShape(3, 4, 2, 13, 12, 3),
                    Block.createCuboidShape(11, 7, 0, 12, 9, 2),
                    Block.createCuboidShape(8, 7, 0, 9, 9, 2),
                    Block.createCuboidShape(7, 7, 0, 8, 9, 2),
                    Block.createCuboidShape(4, 7, 0, 5, 9, 2),
                    Block.createCuboidShape(5, 7, 0, 7, 9, 1),
                    Block.createCuboidShape(9, 7, 0, 11, 9, 1)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            case EAST -> Stream.of(
                    Block.createCuboidShape(13, 4, 3, 14, 12, 13),
                    Block.createCuboidShape(14, 0, 0, 16, 4, 2),
                    Block.createCuboidShape(14, 0, 14, 16, 4, 16),
                    Block.createCuboidShape(0, 0, 14, 2, 4, 16),
                    Block.createCuboidShape(0, 0, 0, 2, 4, 2),
                    Block.createCuboidShape(0, 4, 13, 3, 12, 16),
                    Block.createCuboidShape(13, 4, 13, 16, 12, 16),
                    Block.createCuboidShape(13, 4, 0, 16, 12, 3),
                    Block.createCuboidShape(0, 4, 0, 3, 12, 3),
                    Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                    Block.createCuboidShape(3, 4, 2, 13, 12, 3),
                    Block.createCuboidShape(3, 4, 13, 13, 12, 14),
                    Block.createCuboidShape(2, 4, 3, 3, 12, 13),
                    Block.createCuboidShape(0, 7, 4, 2, 9, 5),
                    Block.createCuboidShape(0, 7, 7, 2, 9, 8),
                    Block.createCuboidShape(0, 7, 8, 2, 9, 9),
                    Block.createCuboidShape(0, 7, 11, 2, 9, 12),
                    Block.createCuboidShape(0, 7, 9, 1, 9, 11),
                    Block.createCuboidShape(0, 7, 5, 1, 9, 7)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            default -> Stream.of(
                    Block.createCuboidShape(2, 4, 3, 3, 12, 13),
                    Block.createCuboidShape(0, 0, 14, 2, 4, 16),
                    Block.createCuboidShape(0, 0, 0, 2, 4, 2),
                    Block.createCuboidShape(14, 0, 0, 16, 4, 2),
                    Block.createCuboidShape(14, 0, 14, 16, 4, 16),
                    Block.createCuboidShape(13, 4, 0, 16, 12, 3),
                    Block.createCuboidShape(0, 4, 0, 3, 12, 3),
                    Block.createCuboidShape(0, 4, 13, 3, 12, 16),
                    Block.createCuboidShape(13, 4, 13, 16, 12, 16),
                    Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                    Block.createCuboidShape(3, 4, 13, 13, 12, 14),
                    Block.createCuboidShape(3, 4, 2, 13, 12, 3),
                    Block.createCuboidShape(13, 4, 3, 14, 12, 13),
                    Block.createCuboidShape(14, 7, 11, 16, 9, 12),
                    Block.createCuboidShape(14, 7, 8, 16, 9, 9),
                    Block.createCuboidShape(14, 7, 7, 16, 9, 8),
                    Block.createCuboidShape(14, 7, 4, 16, 9, 5),
                    Block.createCuboidShape(15, 7, 5, 16, 9, 7),
                    Block.createCuboidShape(15, 7, 9, 16, 9, 11)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        };
    }
}
