package jipthechip.diabolism.blocks;

import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PowderCoveredPolishedBlackstoneBlock extends Block {

    public static final IntProperty PROGRESS = IntProperty.of("progress", 1, 3);
    public PowderCoveredPolishedBlackstoneBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(PROGRESS, 1));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {

        // spawn polished blackstone
        world.spawnEntity(new ItemEntity((World)world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.POLISHED_BLACKSTONE, 1)));
        // spawn stone polishing powder
        world.spawnEntity(new ItemEntity((World)world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(DiabolismItems.STONE_POLISHING_POWDER, 1)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PROGRESS);
    }
}
