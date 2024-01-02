package jipthechip.diabolism.mixin;


import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Wizwich;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.misc.DiabolismDamageSources;
import jipthechip.diabolism.potion.DiabolismEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static jipthechip.diabolism.misc.DiabolismDamageSources.createSquidwardDamageSource;

@Mixin(PlayerEntity.class)
public class PlayerHungerMixin {

    @Shadow
    protected HungerManager hungerManager;


    @Inject(method = "eatFood", at=@At("HEAD"))
    public void wizwich(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir){

        if(stack != null){
            if(stack.getItem() == DiabolismItems.WIZWICH){
                Wizwich wizwich = DataUtils.readObjectFromItemNbt(stack, Wizwich.class);
                if(wizwich != null){
                    int currentHunger = hungerManager.getFoodLevel();
                    int hungerToAdd = wizwich.getIngredients().length();
                    if(hungerToAdd + currentHunger > 30){

                        PlayerEntity player = ((PlayerEntity)(Object)this);
                        // lmao
                        player.addStatusEffect(new StatusEffectInstance(DiabolismEffects.RIGHT_TO_YOUR_THIGHS, 145, 0, true, false, false));
                    }
                    hungerManager.add(hungerToAdd, 0.5f);
                }
            }
        }
    }

}
