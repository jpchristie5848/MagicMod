package jipthechip.diabolism.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatHud.class)
public interface ChatHudAccessor {


    @Invoker("isChatFocused")
    public boolean isChatFocusedInvoker();
}
