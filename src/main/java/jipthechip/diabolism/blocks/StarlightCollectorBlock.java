package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.CrystalAltarBlockEntity;
import jipthechip.diabolism.entities.blockentities.StarlightCollectorBlockEntity;
import jipthechip.diabolism.entities.blockentities.StarterCrystalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class StarlightCollectorBlock extends AbstractMagickaCollectorBlock{

    public StarlightCollectorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StarlightCollectorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, DiabolismEntities.STARLIGHT_COLLECTOR_BLOCKENTITY, StarlightCollectorBlockEntity::ticker);
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
                Block.createCuboidShape(5, 3, 5, 7, 5, 7),
                Block.createCuboidShape(9, 10, 4, 12, 11, 7),
                Block.createCuboidShape(9, 10, 9, 12, 11, 12),
                Block.createCuboidShape(4, 10, 9, 7, 11, 12),
                Block.createCuboidShape(4, 10, 4, 7, 11, 7),
                Block.createCuboidShape(6, 10, 3, 7, 11, 4),
                Block.createCuboidShape(3, 10, 6, 4, 11, 7),
                Block.createCuboidShape(3, 10, 9, 4, 11, 10),
                Block.createCuboidShape(12, 10, 9, 13, 11, 10),
                Block.createCuboidShape(12, 10, 6, 13, 11, 7),
                Block.createCuboidShape(9, 10, 3, 10, 11, 4),
                Block.createCuboidShape(9, 10, 12, 10, 11, 13),
                Block.createCuboidShape(6, 10, 12, 7, 11, 13),
                Block.createCuboidShape(9, 3, 5, 11, 5, 7),
                Block.createCuboidShape(9, 3, 9, 11, 5, 11),
                Block.createCuboidShape(5, 3, 9, 7, 5, 11),
                Block.createCuboidShape(7, 3, 9, 9, 5, 13),
                Block.createCuboidShape(7, 5, 7, 9, 8, 9),
                Block.createCuboidShape(6, 8, 6, 10, 10, 10),
                Block.createCuboidShape(0, 10, 7, 16, 11, 9),
                Block.createCuboidShape(7, 10, 9, 9, 11, 16)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }
}
