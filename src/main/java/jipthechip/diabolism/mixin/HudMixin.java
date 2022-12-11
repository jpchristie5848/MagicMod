package jipthechip.diabolism.mixin;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.hud.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Hud.class)
public interface HudMixin {

    @Accessor("adapter")
    public static OwoUIAdapter<FlowLayout> getAdapter(){
        throw new AssertionError();
    }

    @Invoker("initializeAdapter")
    public static void intializeAdapter() {
        throw new AssertionError();
    }
}
