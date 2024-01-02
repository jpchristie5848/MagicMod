package jipthechip.diabolism.mixin;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTypes.class)
public class DamageTypesMixin {


    @Inject(method="bootstrap", at=@At("HEAD"))
    private static void addDeathTypes(Registerable<DamageType> damageTypeRegisterable, CallbackInfo ci){
        damageTypeRegisterable.register(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("squidward")), new DamageType("squidward", 0.1F));
    }
}
