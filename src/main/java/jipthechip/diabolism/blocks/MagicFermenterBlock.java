package jipthechip.diabolism.blocks;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicFermenterBlock extends AbstractFluidContainingBlock{

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
        return validateTicker(type, DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, MagicFermenter::ticker);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        System.out.println("called fermenter onUse");

        ItemStack stackInHand = player.getStackInHand(hand);

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if(blockEntity instanceof MagicFermenter magicFermenter){
            System.out.println("fermenter: item in hand: "+stackInHand.getItem());

            if(canInteract()){
                if(stackInHand.getItem() == DiabolismItems.BREW_FLUID_BUCKET){
                    if(magicFermenter.addBrewBucket(player, hand)) return ActionResult.SUCCESS;
                }
                else if(stackInHand.getItem() == Items.BUCKET){
                    if(magicFermenter.fillBucket(player, hand)) return ActionResult.SUCCESS;

                }else if(stackInHand.getItem() == DiabolismItems.MYSTICAL_YEAST){
                    if(magicFermenter.addYeastItem(stackInHand, hand, player)) return ActionResult.SUCCESS;
                }
                else if(stackInHand == ItemStack.EMPTY){
                    magicFermenter.toggleOpen();
                    return ActionResult.SUCCESS;
                }
            }
        }
        return world.isClient ? ActionResult.SUCCESS : ActionResult.PASS;
    }
}
