package jipthechip.diabolism.blocks;

import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PROGRESS);
    }
}
