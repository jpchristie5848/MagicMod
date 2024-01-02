package jipthechip.diabolism.items;

import jipthechip.diabolism.items.geo.ArcaneAltarItemRenderer;
import jipthechip.diabolism.items.geo.FluidPumpItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidPumpBlockItem extends BlockItem implements GeoItem {

    private AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation PUMP_NS = RawAnimation.begin().thenPlay("pipe_pump_animation_northsouth");

    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public FluidPumpBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private FluidPumpItemRenderer renderer;
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new FluidPumpItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<FluidPumpBlockItem>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event){
        event.getController().setAnimation(PUMP_NS);
        return PlayState.CONTINUE;
    }
}
