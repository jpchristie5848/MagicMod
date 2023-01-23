package jipthechip.diabolism.items;

import net.minecraft.block.Block;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowderCoveredPolishedBlackstoneBlockItem extends BlockItem {

    int progress;

    public PowderCoveredPolishedBlackstoneBlockItem(Block block, Settings settings) {
        super(block, settings);

    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        NbtCompound nbt = new NbtCompound();
        nbt.putFloat("progress", 0.0f);
        stack.setNbt(nbt);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {

        NbtCompound nbt;
        if(!stack.hasNbt()){
            nbt = new NbtCompound();
            nbt.putFloat("progress", 0.0f);
            stack.setNbt(nbt);
        }else if(!stack.getNbt().contains("progress")){
            nbt = stack.getNbt();
            nbt.putFloat("progress", 0.0f);
            stack.setNbt(nbt);
        }

        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        Block block = world.getBlockState(pos).getBlock();
        PlayerEntity player = context.getPlayer();

        if(block instanceof GrindstoneBlock && ! (player == null) && player.isSneaking()){
            ItemStack stack = player.getStackInHand(context.getHand());
            NbtCompound nbt;
            float progress;
            if(stack.hasNbt()){
                nbt = stack.getNbt();
            }else{
                nbt = new NbtCompound();
            }
            if(nbt.contains("progress")){
                progress = nbt.getFloat("progress")+0.5f;
            }else{
                progress = 0.5f;
            }

            if(progress <= 1.0f)
                nbt.putFloat("progress", progress);
            else
                player.setStackInHand(context.getHand(), new ItemStack(DiabolismItems.DOUBLE_POLISHED_BLACKSTONE_BLOCKITEM, 1));
            stack.setNbt(nbt);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, (float)Math.random()+(1.0f-0.5f)+0.5f, (float)Math.random()+(1.0f-0.5f)+0.5f, true);
            return ActionResult.SUCCESS;
        }else{
            return super.useOnBlock(context);
        }
    }
}
