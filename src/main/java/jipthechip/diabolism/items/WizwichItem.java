package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Wizwich;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DeathMessageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WizwichItem extends Item {

    public WizwichItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Wizwich wizwich = DataUtils.readObjectFromItemNbt(stack, Wizwich.class);

        if(wizwich != null) {
            tooltip.add(1, Text.literal("Ingredients: "+wizwich.getIngredients()).formatted(Formatting.YELLOW));
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {

        return UseAction.EAT;
    }

//    @Override
//    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
//        if(remainingUseTicks == 0 && user instanceof PlayerEntity player){
//            Wizwich wizwich = DataUtils.readObjectFromItemNbt(stack, Wizwich.class);
//            if(wizwich != null){
//                int currentHunger = player.getHungerManager().getFoodLevel();
//                int hungerToAdd = wizwich.getIngredients().length();
//                if(hungerToAdd + currentHunger > 30){ //lmao
//                    world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 10.0f, World.ExplosionSourceType.MOB);
//                }else{
//                    player.getHungerManager().add(hungerToAdd, 0.5f);
//                }
//            }
//        }
//    }
}
