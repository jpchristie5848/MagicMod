package jipthechip.diabolism.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.Random;

public class DiabolismDamageSources {


    public static final RegistryKey<DamageType>[] SQUIDWARD_KEYS = new RegistryKey[]{
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("diabolism", "squidward_0")),
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("diabolism", "squidward_1")),
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("diabolism", "squidward_2"))
    };


    public static DamageSource createSquidwardDamageSource(Entity entity){
        int damangeSourceIndex = (new Random()).nextInt(0,3);
        return entity.getDamageSources().create(SQUIDWARD_KEYS[damangeSourceIndex], entity);
    }
}
