package jipthechip.diabolism.events;

import jipthechip.diabolism.blocks.*;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.potion.DiabolismPotions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;

import java.util.List;

public class DiabolismEvents {

//    private static KeyBinding RightClickKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//            "key.diabolism.rightclick", // The translation key of the keybinding's name
//            InputUtil.Type.MOUSE, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
//            GLFW.GLFW_MOUSE_BUTTON_2, // The keycode of the key
//            "category.diabolism.mouse" // The translation key of the keybinding's category.
//    ));


    public static void registerEvents(){

        //
        // LIGHTNING STRIKE EVENT
        //

        LightningStrikeCallback.EVENT.register(((lightningEntity, blockPos) -> {

            System.out.println("lightning strike event fired");
            BlockState belowState = lightningEntity.world.getBlockState(blockPos.down());

            //
            // Event for activating conductive pillars under lightning rods
            //

            BlockState state = lightningEntity.world.getBlockState(blockPos);
            if(state.getBlock() instanceof LightningRodBlock) {
                if(belowState.getBlock() instanceof AbstractActivatedBlock){
                    if (!belowState.get(AbstractActivatedBlock.ACTIVATED))
                        lightningEntity.world.setBlockState(blockPos.down(),
                                belowState.with(AbstractActivatedBlock.ACTIVATED, true));
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
            //
            // Event for casting basic spells
            //
            if(!world.isClient && stackInHand.getItem() == DiabolismItems.BASIC_WAND){

                //player.addStatusEffect(new StatusEffectInstance(DiabolismPotions.UPDRAFT_STATUS_EFFECT, 100, 0));
                if(player.isSneaking()){
                    List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, Box.of(player.getPos(), 10.0, 10.0, 10.0), entity->true);
                    for(LivingEntity entity : entities){
                        entity.addStatusEffect(new StatusEffectInstance(DiabolismPotions.CHILLY_STATUS_EFFECT, 200, 50));
                    }
                }else{
                    List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, Box.of(player.getPos(), 10.0, 10.0, 10.0), entity->true);
                    for(LivingEntity entity : entities){
                        entity.addStatusEffect(new StatusEffectInstance(DiabolismPotions.WET_STATUS_EFFECT, 200, 0, false, false, true));
                    }
                }

                //player.addStatusEffect(new StatusEffectInstance())

//                if(player.isSneaking()){
//                    ((IMagicProperties)player).toggleMagicShield();
//                    return TypedActionResult.success(stackInHand);
//                }else{
//                    Vec3d direction = Vec3d.fromPolar(player.getPitch(), player.getYaw());
//                    ProjectileSpellEntity spellEntity = new ProjectileSpellEntity(DiabolismEntities.PROJECTILE_SPELL,
//                            player.getX() + (direction.getX() * 3),
//                            player.getY() + (direction.getY() * 3)+1.5,
//                            player.getZ() + (direction.getZ() * 3),
//                            world, direction.multiply(0.2), (float)(0.3));
//
//
//                    world.spawnEntity(spellEntity);

//                    WatcherEntity entity = new WatcherEntity(DiabolismEntities.WATCHER, player.getX(), player.getY()+3, player.getZ(), world, player.getId());
//                    world.spawnEntity(entity);

//                    if(((IMagicProperties)player).isAwakened()){
//                        ((IMagicProperties)player).setAwakened(false);
//                        ((IMagicProperties)player).setMaxMagicka(0);
//                        ((IMagicProperties)player).setMagickaRegenRate(0);
//                        ((IMagicProperties)player).setMagicka(0);
//                    }else{
//                        ((IMagicProperties)player).setAwakened(true);
//                        ((IMagicProperties)player).setMaxMagicka(20);
//                        ((IMagicProperties)player).setMagickaRegenRate(1.0f);
//                    }

                    return TypedActionResult.success(stackInHand);
                //}

            }

            else if(stackInHand.getItem() == Items.MILK_BUCKET){
                if(player.hasStatusEffect(DiabolismPotions.AWAKENING_STATUS_EFFECT)){
                    player.sendMessage(MutableText.of(new LiteralTextContent("Your body rejects it, you can't turn back now")));
                    return TypedActionResult.fail(stackInHand);
                }
            }

            return TypedActionResult.pass(stackInHand);
        });


        //
        // USE BLOCK EVENT
        //

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) ->
        {



                    // tell server to set block to a runed glass block
//                    PacketByteBuf buf = PacketByteBufs.create();
//                    buf.writeBlockPos(hitResult.getBlockPos());
//                    String[] splitBlockName = block.getName().getString().toLowerCase().split(" ");
//                    buf.writeIdentifier(new Identifier("diabolism", "runed_"+splitBlockName[splitBlockName.length-1]));
//                    ClientPlayNetworking.send(DiabolismPackets.SET_BLOCK_PACKET, buf);

            return ActionResult.PASS;
        });

        //
        // PLAYER LOG OUT EVENT
        //

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server)->{
            ((IMagicProperties)handler.player).deinitialize();
        });


        ClientEntityEvents.ENTITY_LOAD.register((entity, world)->{
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(entity.getId());
            ClientPlayNetworking.send(DiabolismPackets.GET_ENTITY_EFFECTS_FROM_SERVER, buf);
        });

        //
        // REGISTER UI EVENT HANDLERS
        //
        UIEvents.registerEvents();
    }

}
