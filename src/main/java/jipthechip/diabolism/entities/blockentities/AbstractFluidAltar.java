package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.blocks.AbstractActivatedBlock;
import jipthechip.diabolism.entities.entityrecipe.BlockEntityRecipe;
import jipthechip.diabolism.entities.entityrecipe.RecipeBlockResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFluidAltar extends AbstractFluidContainer  implements IBlockEntityWithRecipe, IBlockEntityWithConnections{


    private int tickCounter = 0;
    private int lastActivated = 0;

    private int storedItemLastUpdated = 0;

    private final BlockEntityRecipe[] recipes;

    private List<BlockPos> connectedEntities = new ArrayList<>();

    private Identifier storedItem;

    public AbstractFluidAltar(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, BlockEntityRecipe[] recipes) {
        super(type, pos, state, capacity, true, null);
        this.recipes = recipes;
    }

    protected void tick(World world, BlockPos pos, BlockState state) {

        tickCounter++;
        if(tickCounter % 20 == 0){
            //System.out.println("ticked crystal altar entity");
            if(state.get(AbstractActivatedBlock.ACTIVATED)){
                if(tickCounter - lastActivated > 40){
                    System.out.println("last activated: "+lastActivated);
                    System.out.println("altar hasn't been activated, disabling");
                    world.setBlockState(pos, state.with(AbstractActivatedBlock.ACTIVATED, false));
                }
            }else if(tickCounter - lastActivated <= 40 && lastActivated > 0){
                world.setBlockState(pos, state.with(AbstractActivatedBlock.ACTIVATED, true));
            }
            //amount = (int)((((Math.sin(tickCounter/200.0)/2)+0.5)*capacity));
            BlockEntityRecipe recipe = checkRecipes();
            if(recipe != null){
                ItemStack stack = recipe.getItemResult(this);
                for(RecipeBlockResult blockResult : recipe.getBlockResult(this)){
                    world.setBlockState(blockResult.getPos(), blockResult.getState());
                }
                removeFluid(recipe.getFluidCost(this));
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY()+1.0f, pos.getZ()+0.5f, stack));
                this.setStoredItem(null);
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag){
        if (this.storedItem == null){
            tag.putString("item", "");
        }else{
            tag.putString("item", storedItem.getNamespace()+":"+storedItem.getPath());
        }
        List<Integer> posIntList = new ArrayList<>();
        for (BlockPos pos : connectedEntities){
            posIntList.add(pos.getX());
            posIntList.add(pos.getY());
            posIntList.add(pos.getZ());
        }
        tag.putIntArray("connectedEntities", posIntList);
        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag){
        super.readNbt(tag);
        String storedItemStr = tag.getString("item");
        storedItem = storedItemStr == null || storedItemStr.isEmpty() ? null : new Identifier(storedItemStr.split(":")[0],storedItemStr.split(":")[1]);

        int[] posIntArr = tag.getIntArray("connectedEntities");
        connectedEntities = new ArrayList<>();
        for(int i = 0; i < posIntArr.length/3; i++){
            connectedEntities.add(new BlockPos(posIntArr[(i*3)], posIntArr[(i*3)+1], posIntArr[(i*3)+2]));
        }
    }

    public Item getStoredItem(){
        return storedItem == null ? null : Registries.ITEM.get(storedItem);
    }

    public boolean setStoredItem(@Nullable Item item){
        if(item == null){
            storedItem = null;
        }
        else if (storedItemIsUpdatable()){
            if(storedItem != null && world != null){
                world.spawnEntity(new ItemEntity(world, (double)this.getPos().getX()+0.5, (double)this.getPos().getY()+1, (double)this.getPos().getZ()+0.5, new ItemStack(Registries.ITEM.get(storedItem))));
                storedItem = null;
                return false;
            }else{
                storedItem = Registries.ITEM.getId(item);
                storedItemLastUpdated = tickCounter;
                return true;
            }
        }
        return false;
    }



    public boolean storedItemIsUpdatable(){
        int ITEM_UPDATE_COOLDOWN = 20;
        return tickCounter > storedItemLastUpdated + ITEM_UPDATE_COOLDOWN;
    }

    public void markActivated(){
        lastActivated = tickCounter;
    }

    @Override
    public BlockEntityRecipe checkRecipes() {
        for(BlockEntityRecipe recipe : recipes){
            if(recipe.requirementsMet(this)) return recipe;
        }
        return null;
    }

    public void addConnectedEntity(BlockPos pos){
        if(!connectedEntities.contains(pos))connectedEntities.add(pos);
    }

    public void removeConnectedEntity(BlockPos pos){
        connectedEntities.remove(pos);
    }

    public List<BlockPos> getConnectedEntities(){ return connectedEntities; }

//    public void syncWithServer(){
//
//    }
}
