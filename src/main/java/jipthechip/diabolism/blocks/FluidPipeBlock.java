package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import jipthechip.diabolism.entities.blockentities.CrystalAltarBlockEntity;
import jipthechip.diabolism.entities.blockentities.FluidPipe;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;

public class FluidPipeBlock extends AbstractOmniDirectionalBlock {

    public FluidPipeBlock(Settings settings) {
        super(settings);
        MAX_CONNECTIONS = 6;

        CENTER_SHAPES = Collections.singletonList(Block.createCuboidShape(6, 6, 6, 10, 10, 10));
        NORTH_SHAPES = Collections.singletonList(Block.createCuboidShape(6, 6, 0, 10, 10, 6));
        SOUTH_SHAPES = Collections.singletonList(Block.createCuboidShape(6, 6, 10, 10, 10, 16));
        EAST_SHAPES = Collections.singletonList(Block.createCuboidShape(10, 6, 6, 16, 10, 10));
        WEST_SHAPES = Collections.singletonList(Block.createCuboidShape(0, 6, 6, 6, 10, 10));
        UP_SHAPES = Collections.singletonList(Block.createCuboidShape(6, 10, 6, 10, 16, 10));
        DOWN_SHAPES = Collections.singletonList(Block.createCuboidShape(6, 0, 6, 10, 6, 10));

        DIRECTION_SHAPES = new HashMap<>(){{
            put(Direction.NORTH, NORTH_SHAPES);
            put(Direction.SOUTH, SOUTH_SHAPES);
            put(Direction.EAST, EAST_SHAPES);
            put(Direction.WEST, WEST_SHAPES);
            put(Direction.UP, UP_SHAPES);
            put(Direction.DOWN, DOWN_SHAPES);
        }};

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        checkConnections(world, pos, state, AbstractFluidContainer.class, null);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidPipe(pos, state);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, DiabolismEntities.FLUID_PIPE_BLOCKENTITY, FluidPipe::ticker);
    }
}
