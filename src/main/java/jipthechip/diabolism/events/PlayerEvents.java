package jipthechip.diabolism.events;

import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.data.spell.SpellType;
import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class PlayerEvents {

    public static void registerEvents(){

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

                Spell[] testSpells = new Spell[]{
//                        new Spell(SpellType.PROJECTILE, new StatusEffectInstance(DiabolismEffects.UPDRAFT_STATUS_EFFECT, 100, 50)),
//                        new Spell(SpellType.PROJECTILE, new StatusEffectInstance(DiabolismEffects.SHOCK_STATUS_EFFECT, 0, 50)),
                        new Spell(SpellType.PROJECTILE, new StatusEffectInstanceData("chilly", 100, 50)),
                        new Spell(SpellType.PROJECTILE, new StatusEffectInstanceData("wet", 100, 0)),
//                        new Spell(SpellType.PROJECTILE, new StatusEffectInstance(DiabolismEffects.BURNING_STATUS_EFFECT, 100, 50)),
//                        new Spell(SpellType.PROJECTILE, new StatusEffectInstance(DiabolismEffects.SHOCK_STATUS_EFFECT, 0, 50), new StatusEffectInstance(DiabolismEffects.BURNING_STATUS_EFFECT, 100, 50)),
                        //new Spell(SpellType.PROJECTILE, new StatusEffectInstance(DiabolismEffects.BROKEN_BONES_STATUS_EFFECT, 500, 50)),
                        //new Spell(SpellType.SELF, new StatusEffectInstance(DiabolismEffects.LIFE_STATUS_EFFECT, 100, 60)),
                        //new Spell(SpellType.SELF, new StatusEffectInstance(DiabolismEffects.DEATH_STATUS_EFFECT, 100, 20))
                };

                double random = Math.random();
                float sum = 0;
                int chosenSpellIndex = 0;
                for (int i = 0; i < testSpells.length; i++){
                    sum += 1.0f/ testSpells.length;
                    if(random < sum){
                        chosenSpellIndex = i;
                        break;
                    }
                }

                testSpells[chosenSpellIndex].cast(player);

                return TypedActionResult.success(stackInHand);
            }

            else if(stackInHand.getItem() == Items.MILK_BUCKET){
                if(player.hasStatusEffect(DiabolismEffects.MAP.get("awakening"))){
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

            return ActionResult.PASS;
        });
    }
}
