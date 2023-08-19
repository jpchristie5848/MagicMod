package jipthechip.diabolism.mixin;


import jipthechip.diabolism.potion.DiabolismPotions;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mixin(AbstractInventoryScreen.class)
public class DrawStatusEffectsMixin {

    private final List<StatusEffect> EFFECTS_TO_REMOVE = Arrays.asList(DiabolismPotions.MAGIC_HARM_STATUS_EFFECT);

    @ModifyVariable(method="drawStatusEffects", at=@At("STORE"), ordinal = 0)
    private Collection<StatusEffectInstance> modifyDrawnStatusEffects(Collection<StatusEffectInstance> c){
        c.removeIf((instance) -> EFFECTS_TO_REMOVE.contains(instance.getEffectType()));
        return c;
    }
}
