package jipthechip.diabolism.blocks;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.data.Yeast;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.MagicFermenter;
import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicFermenterBlock extends AbstractFluidContainingBlock{

    private long lastInteractionTime = 0;
    protected MagicFermenterBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicFermenter(pos, state);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, MagicFermenter::ticker);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        System.out.println("called fermenter onUse");

        ItemStack stackInHand = player.getStackInHand(hand);
        //ActionResult superResult = super.onUse(state, world, pos, player, hand, hit);


        BlockEntity blockEntity = world.getBlockEntity(pos);


        if(blockEntity instanceof MagicFermenter magicFermenter){
            System.out.println("fermenter: item in hand: "+stackInHand.getItem());
            if(stackInHand.getItem() == DiabolismItems.BREW_FLUID_BUCKET && System.currentTimeMillis() - lastInteractionTime >= 500){
                Fluid fluid = DataUtils.readObjectFromItemNbt(stackInHand, Fluid.class);

                if(fluid != null){
                    //System.out.println("fluid not null in abstract onUse");
                    int fluidAdded = magicFermenter.addFluid(fluid);
                    if(fluidAdded > 0){
                        //magicFermenter.syncWithServer();
                        if(fluidAdded < fluid.getAmount()){
                            fluid.remove(fluidAdded);
                            DataUtils.writeObjectToItemNbt(stackInHand, fluid);
                            System.out.println("abstract onUse: removing only partial fluid from bucket: "+fluidAdded);
                        }else{
                            System.out.println("abstract onUse: set item stack to empty bucket");
                            player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        }

                        return ActionResult.SUCCESS;
                    }
                }

            }
            else if(stackInHand.getItem() == Items.BUCKET && magicFermenter.getFluid() != null && System.currentTimeMillis() - lastInteractionTime >= 500){
                //if(!player.isCreative()){
                ItemStack brewBucketStack = new ItemStack(DiabolismItems.BREW_FLUID_BUCKET);
                DataUtils.writeObjectToItemNbt(brewBucketStack, new Fluid(magicFermenter.getFluid()));

                player.setStackInHand(Hand.MAIN_HAND, brewBucketStack);

                System.out.println("fermenter: set stack in hand to brew fluid bucket: ");
                //}
                magicFermenter.emptyFluid();
                magicFermenter.setYeast(null);
                //magicFermenter.syncWithServer();

                return ActionResult.SUCCESS;

            }else if(stackInHand.getItem() == DiabolismItems.MYSTICAL_YEAST && magicFermenter.getFluid() != null && System.currentTimeMillis() - lastInteractionTime >= 500){

                Yeast yeast = DataUtils.readObjectFromItemNbt(stackInHand, Yeast.class);
                if(yeast != null){
                    magicFermenter.setYeast(yeast);
                    player.setStackInHand(hand, ItemStack.EMPTY);
                    magicFermenter.markDirty();
                }
            }
            else{
                magicFermenter.toggleOpen();
                magicFermenter.markDirty();
                //System.out.println("Fermenter fluid: "+(magicFermenter.getFluid() == null ? "null" : magicFermenter.getFluid()));
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
