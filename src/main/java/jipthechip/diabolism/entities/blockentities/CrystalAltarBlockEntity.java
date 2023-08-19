package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.entityrecipe.BasicAltarRecipe;
import jipthechip.diabolism.entities.entityrecipe.BlockEntityRecipe;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.render.RenderDataMappings;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class CrystalAltarBlockEntity extends AbstractFluidAltar{

    public CrystalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(DiabolismEntities.CRYSTAL_ALTAR_BLOCKENTITY, pos, state, 1000,
                new BlockEntityRecipe[]{
                        new BasicAltarRecipe(RenderDataMappings.Fluids.get("blood"), 300,
                                new ItemStack(Items.STICK), new ItemStack(DiabolismItems.BASIC_WAND)),
                        new BasicAltarRecipe(RenderDataMappings.Fluids.get("sunlight"), 300,
                                new ItemStack(Items.AMETHYST_SHARD), new ItemStack(DiabolismItems.SOLAR_SHARD)),
                        new BasicAltarRecipe(RenderDataMappings.Fluids.get("starlight"), 300,
                                new ItemStack(Items.AMETHYST_SHARD), new ItemStack(DiabolismItems.STAR_SHARD))
                });
    }



    public static void ticker(World world, BlockPos pos, BlockState state, CrystalAltarBlockEntity be) {
        be.tick(world, pos, state);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
