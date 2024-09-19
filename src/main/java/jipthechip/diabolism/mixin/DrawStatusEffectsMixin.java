package jipthechip.diabolism.mixin;


import jipthechip.diabolism.effect.DiabolismEffects;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(AbstractInventoryScreen.class)
public class DrawStatusEffectsMixin {

    @Unique
    private final List<StatusEffect> EFFECTS_TO_REMOVE = Arrays.asList(DiabolismEffects.MAP.get("harm"), DiabolismEffects.MAP.get("righttoyourthighs"));

    @ModifyVariable(method="drawStatusEffects", at=@At("STORE"), ordinal = 0)
    private Collection<StatusEffectInstance> modifyDrawnStatusEffects(Collection<StatusEffectInstance> c){
        c.removeIf((instance) -> EFFECTS_TO_REMOVE.contains(instance.getEffectType()));
        return c;
    }
}
