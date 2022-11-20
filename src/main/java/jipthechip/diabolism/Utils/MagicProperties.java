package jipthechip.diabolism.Utils;

import jipthechip.diabolism.entities.ShieldSpellEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface MagicProperties {

    public int getMagicShield();

    public void setMagicShield(int entityId);

    public void toggleMagicShield();

    public void deinitialize();
}
