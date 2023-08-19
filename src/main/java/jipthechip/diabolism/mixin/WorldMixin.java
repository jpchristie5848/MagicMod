package jipthechip.diabolism.mixin;

import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.function.Supplier;

@Mixin(World.class)
public abstract class WorldMixin {
//    protected WorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
//        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
//    }
//
//    private boolean isThundering;
//
//    @Override
//    public float getRainGradient(float delta) {
//        if (!isThundering) {
//            return 0;
//        } else if (isThundering) {
//            return 1;
//        }
//        return super.getRainGradient(delta);
//    }
//
//    @Override
//    public float getThunderGradient(float delta) {
//        if (mod.mode == WeatherChanger.Mode.CLEAR) {
//            return 0;
//        } else if (mod.mode == WeatherChanger.Mode.THUNDER) {
//            return 1;
//        }
//
//        return super.getRainGradient(delta);
//    }
//    //@Inject(method="getOtherEntities", at=@At(value = "INVOKE", target=""))
}
