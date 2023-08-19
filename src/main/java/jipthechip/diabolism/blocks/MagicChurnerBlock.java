package jipthechip.diabolism.blocks;

import jipthechip.diabolism.data.BrewIngredient;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.InventoryHelpers;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.MagicChurner;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.particle.ColoredSplashParticleFactory;
import jipthechip.diabolism.render.RenderDataMappings;
import jipthechip.diabolism.sound.DiabolismSounds;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

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
        return checkType(type, DiabolismEntities.MAGIC_CHURNER_BLOCKENTITY, MagicChurner::ticker);
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

            if(magicChurner != null){
                if(stackInMainHand.getItem() == Items.WATER_BUCKET && magicChurner.getFluidRenderData() == null){

                    magicChurner.addFluid(1000, RenderDataMappings.Fluids.get("churner_fluid"));
                    if(!player.isCreative()){
                        player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                    }
                    world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f, true);
                    //magicChurner.syncWithServer();

                    return ActionResult.SUCCESS;
                }else if (stackInMainHand.getItem() == Items.BUCKET && magicChurner.getFluidRenderData() != null){

                    //if(!player.isCreative()){
                        ItemStack brewBucketStack = new ItemStack(DiabolismItems.BREW_FLUID_BUCKET);
                        DataUtils.writeObjectToItemNbt(brewBucketStack, magicChurner.getFluid());
                        player.setStackInHand(Hand.MAIN_HAND, brewBucketStack);
                        System.out.println("churner: set item in hand to brew bucket stack");
                    //}
                    magicChurner.removeFluid(magicChurner.getFluidAmount());
                    if(magicChurner.getMixingProgress() < 100){
                        System.out.println("mixing progress: "+magicChurner.getMixingProgress());
                        for(ItemStack stack : magicChurner.getItems()){
                            world.spawnEntity(new ItemEntity(world, pos.getX()+0.5f, pos.getY()+1.2f, pos.getZ()+0.5f, stack));
                        }
                    }
                    magicChurner.clearInventory();
                    world.playSoundAtBlockCenter(pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f, true);
                    magicChurner.setMixingProgress(0);
                    //magicChurner.syncWithServer();

                    return ActionResult.SUCCESS;
                }else if(BrewIngredient.mappings.containsKey(stackInMainHand.getItem()) && magicChurner.getFluidRenderData() != null && magicChurner.getMixingProgress() < 100) {

                    int slot = magicChurner.getFirstEmptySlot();

                    if(slot > -1){
                        magicChurner.setStack(slot, InventoryHelpers.getItemsFromStackInHand(player, hand, 1));
                        world.playSoundAtBlockCenter(pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1.0f, (float)Math.random()*0.5f+0.5f, true);
                        magicChurner.setMixingProgress(0);

                        Vec3d splashPos = new Vec3d(pos.getX()+0.15 + Math.random()*0.7, pos.getY()+0.9, pos.getZ()+0.15 + Math.random()*0.7);

                        for (int i = 0; i < 15; i++){
                            world.addParticle(ColoredSplashParticleFactory.createData(magicChurner.getFluidColor()), splashPos.getX(), splashPos.getY(), splashPos.getZ(), 0, 0,0);
                        }
                        return ActionResult.SUCCESS;
                    }
                }else if(stackInOffHand.getItem() != Items.BUCKET && stackInOffHand.getItem() != Items.WATER_BUCKET && System.currentTimeMillis() - lastTurnedTime > 3500){

                    lastTurnedTime = System.currentTimeMillis();
                    magicChurner.triggerAnim("magic_churner_controller", "magic_churner_turn");
                    if(magicChurner.getFluidRenderData() != null){
                        world.playSoundAtBlockCenter(pos, DiabolismSounds.CHURNER_SLOSH, SoundCategory.BLOCKS, 1.5f, (float)1.0f, true);
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeBlockPos(pos);
                        ClientPlayNetworking.send(DiabolismPackets.BEGIN_CHURNER_MIXING, buf);
                    }

                    if(magicChurner.getFluid() != null){
                        System.out.println("magic churner amount: "+magicChurner.getFluidAmount());
                        System.out.println("magic churner mixing progress: "+magicChurner.getMixingProgress());
                        System.out.println("magic churner color: "+magicChurner.getFluidColor());
                        System.out.println("magic churner element contents: "+Arrays.toString(magicChurner.getElementContents()));
                    }

                    return ActionResult.SUCCESS;
                }
            }
        return ActionResult.PASS;
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
