package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.StarterCrystalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class StarterCrystalBlock extends AbstractActivatedBlock {

    public StarterCrystalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Stream.of(
                Block.createCuboidShape(4, 8, 4, 12, 13, 12),
                Block.createCuboidShape(5, 13, 5, 11, 15, 11),
                Block.createCuboidShape(3, 1, 3, 13, 8, 13),
                Block.createCuboidShape(0, 0, 0, 16, 1, 16),
                Block.createCuboidShape(6, 1, 2, 10, 2, 3),
                Block.createCuboidShape(7, 2, 2, 9, 9, 3),
                Block.createCuboidShape(7, 13, 4, 9, 15, 5),
                Block.createCuboidShape(4, 13, 7, 5, 15, 9),
                Block.createCuboidShape(4, 15, 4, 12, 16, 12),
                Block.createCuboidShape(3, 8, 7, 4, 14, 9),
                Block.createCuboidShape(2, 1, 6, 3, 2, 10),
                Block.createCuboidShape(6, 1, 13, 10, 2, 14),
                Block.createCuboidShape(7, 2, 13, 9, 9, 14),
                Block.createCuboidShape(7, 13, 11, 9, 15, 12),
                Block.createCuboidShape(13, 1, 6, 14, 2, 10),
                Block.createCuboidShape(13, 2, 7, 14, 9, 9),
                Block.createCuboidShape(11, 13, 7, 12, 15, 9),
                Block.createCuboidShape(2, 2, 7, 3, 9, 9),
                Block.createCuboidShape(7, 8, 12, 9, 14, 13),
                Block.createCuboidShape(12, 8, 7, 13, 14, 9),
                Block.createCuboidShape(7, 8, 3, 9, 14, 4)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, DiabolismEntities.STARTER_CRYSTAL_BLOCKENTITY, StarterCrystalBlockEntity::ticker);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StarterCrystalBlockEntity(pos, state);
    }
}
