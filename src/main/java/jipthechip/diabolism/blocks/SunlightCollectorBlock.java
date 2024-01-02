package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.StarlightCollectorBlockEntity;
import jipthechip.diabolism.entities.blockentities.SunlightCollectorBlockEntity;
import net.minecraft.block.*;
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

public class SunlightCollectorBlock extends AbstractMagickaCollectorBlock {
    protected SunlightCollectorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SunlightCollectorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, DiabolismEntities.SUNLIGHT_COLLECTOR_BLOCKENTITY, SunlightCollectorBlockEntity::ticker);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Stream.of(
                Block.createCuboidShape(7, 10, 0, 9, 11, 7),
                Block.createCuboidShape(1, 0, 1, 15, 1, 15),
                Block.createCuboidShape(3, 1, 3, 13, 3, 13),
                Block.createCuboidShape(3, 3, 7, 13, 5, 9),
                Block.createCuboidShape(7, 3, 3, 9, 5, 7),
                Block.createCuboidShape(4, 3, 4, 7, 5, 7),
                Block.createCuboidShape(9, 10, 2, 14, 11, 7),
                Block.createCuboidShape(9, 10, 9, 14, 11, 14),
                Block.createCuboidShape(2, 10, 9, 7, 11, 14),
                Block.createCuboidShape(2, 10, 2, 7, 11, 7),
                Block.createCuboidShape(4, 10, 1, 7, 11, 2),
                Block.createCuboidShape(1, 10, 4, 2, 11, 7),
                Block.createCuboidShape(1, 10, 9, 2, 11, 12),
                Block.createCuboidShape(14, 10, 9, 15, 11, 12),
                Block.createCuboidShape(14, 10, 4, 15, 11, 7),
                Block.createCuboidShape(9, 10, 1, 12, 11, 2),
                Block.createCuboidShape(9, 10, 14, 12, 11, 15),
                Block.createCuboidShape(4, 10, 14, 7, 11, 15),
                Block.createCuboidShape(9, 3, 4, 12, 5, 7),
                Block.createCuboidShape(9, 3, 9, 12, 5, 12),
                Block.createCuboidShape(4, 3, 9, 7, 5, 12),
                Block.createCuboidShape(7, 3, 9, 9, 5, 13),
                Block.createCuboidShape(7, 5, 7, 9, 8, 9),
                Block.createCuboidShape(6, 8, 6, 10, 10, 10),
                Block.createCuboidShape(0, 10, 7, 16, 11, 9),
                Block.createCuboidShape(7, 10, 9, 9, 11, 16)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }

}
