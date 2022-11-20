package jipthechip.diabolism.events;

import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.blocks.AbstractAltarBlock;
import jipthechip.diabolism.blocks.AbstractAltarComponentBlock;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.entities.ShieldSpellEntity;
import jipthechip.diabolism.entities.blockentities.AltarBlockEntity;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.items.RunicPowder;
import jipthechip.diabolism.Utils.MagicProperties;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class DiabolismEvents {

    private static KeyBinding RightClickKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.diabolism.rightclick", // The translation key of the keybinding's name
            InputUtil.Type.MOUSE, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_MOUSE_BUTTON_2, // The keycode of the key
            "category.diabolism.mouse" // The translation key of the keybinding's category.
    ));

    public static void registerEvents(){

        //
        // LIGHTNING STRIKE EVENT
        //

        LightningStrikeCallback.EVENT.register(((lightningEntity, blockPos) -> {

            BlockState belowState = lightningEntity.world.getBlockState(blockPos.down());

            if(belowState.getBlock() == DiabolismBlocks.CONDUCTIVE_PILLAR){
                BlockState state = lightningEntity.world.getBlockState(blockPos);
                if(state.getBlock() == Blocks.LIGHTNING_ROD) {
                    if (!belowState.get(AbstractAltarComponentBlock.ACTIVATED))
                        lightningEntity.world.setBlockState(blockPos.down(),
                                belowState.with(AbstractAltarComponentBlock.ACTIVATED, true));
                }
            }

            return ActionResult.SUCCESS;
        }));

//        ClientTickEvents.END_CLIENT_TICK.register((client -> {
//
//            // checks if key released?
//            if(RightClickKeyBinding.wasPressed() && !RightClickKeyBinding.isPressed()){
//                System.out.println("Right click released");
//                assert client.player != null;
//                int magicShieldEntityId = ((MagicProperties)client.player).getMagicShield();
//
//                if(magicShieldEntityId != -1){
//                    ShieldSpellEntity entity = (ShieldSpellEntity) client.world.getEntityById(magicShieldEntityId);
//                    assert entity != null;
//                    entity.kill();
//                    PacketByteBuf buf = PacketByteBufs.create();
//                    buf.writeInt(magicShieldEntityId);
//
//                    ClientPlayNetworking.send(DiabolismPackets.KILL_ENTITY_PACKET, buf);
//
//                    ((MagicProperties)client.player).setMagicShield(-1);
//                }
//            }
//        }));

        //
        // USE ITEM EVENT
        //

        UseItemCallback.EVENT.register((player, world, hand)->
        {
            ItemStack stackInHand = player.getStackInHand(hand);

            if(stackInHand.getItem() == DiabolismItems.BASIC_WAND){

                if(player.isSneaking()){
                    ((MagicProperties)player).toggleMagicShield();
                }else{
                    //System.out.println("pitch: "+player.getPitch());
                    //System.out.println("yaw: "+player.getYaw());
                    Vec3d direction = Vec3d.fromPolar(player.getPitch(), player.getYaw());
                    ProjectileSpellEntity spellEntity = new ProjectileSpellEntity(DiabolismEntities.PROJECTILE_SPELL,
                            player.getX() + (direction.getX() * 2),
                            player.getY() + (direction.getY() * 2)+1,
                            player.getZ() + (direction.getZ() * 2),
                            world, direction.multiply(0.3));

                    world.spawnEntity(spellEntity);
                }

            }

            return TypedActionResult.pass(stackInHand);
        });


        //
        // USE BLOCK EVENT
        //

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) ->
        {

            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();

            // check if used block is a glass block
            if(block.equals(Blocks.GLASS) || block.equals(Blocks.COPPER_BLOCK)){

                ItemStack stack = player.getStackInHand(hand);

                // check if player is holding runic powder
                if(stack.getItem() instanceof RunicPowder){

                    // tell server to set block to a runed glass block
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeBlockPos(hitResult.getBlockPos());
                    String[] splitBlockName = block.getName().getString().toLowerCase().split(" ");
                    buf.writeIdentifier(new Identifier("diabolism", "runed_"+splitBlockName[splitBlockName.length-1]));
                    ClientPlayNetworking.send(DiabolismPackets.SET_BLOCK_PACKET, buf);

                    // remove one runic powder from player's hand
                    if(stack.getCount() > 1){
                        stack.setCount(stack.getCount()-1);
                        player.setStackInHand(hand, stack);
                    }else{
                        player.setStackInHand(hand, ItemStack.EMPTY);
                    }

                    return ActionResult.SUCCESS;
                }
            }else if(block instanceof AbstractAltarBlock) {

                BlockPos pos = hitResult.getBlockPos();
                AltarBlockEntity blockEntity = (AltarBlockEntity) world.getBlockEntity(pos);

                if(blockEntity != null && blockEntity.storedItemUpdatable()){

                    ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                    BlockState state = world.getBlockState(pos);
                    Item storedItem = blockEntity.getStoredItem();

                    if(storedItem != Items.AIR && stack.getItem() != storedItem){
                        if(player.isSneaking()) {
                            blockEntity.setStoredItem(Items.AIR);
                            if (stack == ItemStack.EMPTY) {
                                player.setStackInHand(Hand.MAIN_HAND, new ItemStack(storedItem, 1));
                            } else {
                                world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(storedItem, 1)));
                            }
                            blockEntity.markDirty();
                            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                            return ActionResult.SUCCESS;
                        }
                    }else if(stack != ItemStack.EMPTY && storedItem == Items.AIR){
                        blockEntity.setStoredItem(stack.getItem());
                        if(stack.getCount() > 1){
                            stack.setCount(stack.getCount()-1);
                            player.setStackInHand(Hand.MAIN_HAND, stack);
                        }else{
                            player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        }
                        blockEntity.markDirty();
                        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });

        //
        // PLAYER LOG OUT EVENT
        //

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server)->{
            ((MagicProperties)handler.player).deinitialize();
        });
    }
}
