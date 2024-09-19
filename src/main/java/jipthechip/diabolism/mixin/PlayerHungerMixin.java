package jipthechip.diabolism.mixin;


import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Wizwich;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.effect.DiabolismEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
                        player.addStatusEffect(new StatusEffectInstance(DiabolismEffects.MAP.get("right_to_your_thighs"), 145, 0, true, false, false));
                    }
                    hungerManager.add(hungerToAdd, 0.5f);
                }
            }
        }
    }

}
