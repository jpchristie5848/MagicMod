package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.entities.DiabolismEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StarterCrystalBlockEntity extends AbstractActivatedPowerSource{

    public StarterCrystalBlockEntity(BlockPos pos, BlockState state) {
        super(DiabolismEntities.STARTER_CRYSTAL_BLOCKENTITY, pos, state);
    }

    public static void ticker(World world, BlockPos pos, BlockState state, StarterCrystalBlockEntity be) {
        be.tick(world, pos, state);
    }

}
