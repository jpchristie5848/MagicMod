package jipthechip.diabolism.entities.entityrecipe;


import jipthechip.diabolism.blocks.AbstractActivatedBlock;
import jipthechip.diabolism.entities.blockentities.AbstractActivatedPowerSource;
import jipthechip.diabolism.entities.blockentities.AbstractFluidAltar;
import jipthechip.diabolism.render.FluidRenderData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicAltarRecipe implements BlockEntityRecipe {

    FluidRenderData fluidInput;
    int fluidRequired;

    ItemStack itemInput;
    ItemStack result;

    BlockPos powerSourcePos;

    public BasicAltarRecipe(FluidRenderData fluidInput, int amountRequired, ItemStack itemInput, ItemStack result){
        this.fluidInput = fluidInput;
        this.fluidRequired = amountRequired;
        this.itemInput = itemInput;
        this.result = result;
    }
    @Override
    public ItemStack getItemResult(BlockEntity be) {
        return result;
    }



    @Override
    public RecipeBlockResult[] getBlockResult(BlockEntity be) {
        return powerSourcePos == null ? new RecipeBlockResult[]{new RecipeBlockResult(be.getPos(), be.getCachedState().with(AbstractActivatedBlock.ACTIVATED, false))}
                : new RecipeBlockResult[]{new RecipeBlockResult(powerSourcePos, be.getWorld().getBlockState(powerSourcePos).with(AbstractActivatedBlock.ACTIVATED, false))};
    }

    @Override
    public int getFluidCost(BlockEntity be) {
        return fluidRequired;
    }


    @Override
    public boolean requirementsMet(BlockEntity be) {
        AbstractFluidAltar fluidAltar = (AbstractFluidAltar) be;
        World world = fluidAltar.getWorld();

        for(BlockPos pos : fluidAltar.getConnectedEntities()){
            BlockEntity posBE = world.getBlockEntity(pos);

            if(posBE instanceof AbstractActivatedPowerSource && world.getBlockState(pos).get(AbstractActivatedBlock.ACTIVATED)){
                powerSourcePos = pos;
                break;
            }
        }

        FluidRenderData fluid = fluidAltar.getFluidRenderData();

        return fluid != null && fluid.equals(fluidInput) && fluidAltar.getFluidAmount() >= fluidRequired && be.getCachedState().get(AbstractActivatedBlock.ACTIVATED) && fluidAltar.getStoredItem() == itemInput.getItem();
    }
}
