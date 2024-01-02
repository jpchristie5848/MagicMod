package jipthechip.diabolism.blocks;

import jipthechip.diabolism.data.BrewIngredient;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.MagicChurner;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MagicChurnerBlock extends AbstractFluidContainingBlock {

    private long lastTurnedTime = 0;
    protected MagicChurnerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicChurner(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, DiabolismEntities.MAGIC_CHURNER_BLOCKENTITY, MagicChurner::ticker);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

            MagicChurner magicChurner = (MagicChurner) world.getBlockEntity(pos);

            ItemStack stackInMainHand = player.getStackInHand(hand);
            ItemStack stackInOffHand = player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);

            Item itemInHand = stackInMainHand.getItem();

            System.out.println("World is client: "+world.isClient);

            if(magicChurner != null){

                // add water bucket to churner
                if(itemInHand == Items.WATER_BUCKET){
                    if(magicChurner.addWaterBucket(player, hand)) return ActionResult.SUCCESS;
                }
                // fill empty bucket with churner contents
                else if (itemInHand == Items.BUCKET){
                    if(magicChurner.fillBucket(player, hand)) return ActionResult.SUCCESS;
                }
                // add brew ingredient to churner
                else if(BrewIngredient.isValidIngredient(itemInHand)) {

                    if(magicChurner.addIngredient(player, hand)) return ActionResult.SUCCESS;

                // mix churner
                }else if(stackInOffHand.getItem() != Items.BUCKET && stackInOffHand.getItem() != Items.WATER_BUCKET){

                    magicChurner.turn();

                    return ActionResult.SUCCESS;
                }
            }
        return world.isClient ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        MagicChurner entity = (MagicChurner) world.getBlockEntity(pos);
        if(entity != null && entity.getMixingProgress() < 100){
            for(ItemStack stack : entity.getItems()){
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f, stack));
            }
        }
    }

}
