package jipthechip.diabolism.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public abstract class RunedBlock extends Block {

    public static final BooleanProperty HAS_RUNES = BooleanProperty.of("has_runes");

    public RunedBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(HAS_RUNES, false));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_RUNES);
    }

}
