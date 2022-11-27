package jipthechip.diabolism.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.class)
public interface ItemAccessor {
    @Invoker("getRecipeRemainder")
    public static Item getRecipeRemainder(){
        throw new AssertionError();
    }

}
