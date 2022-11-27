package jipthechip.diabolism.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(World.class)
public abstract class WorldMixin {

    //@Inject(method="getOtherEntities", at=@At(value = "INVOKE", target=""))
}
