package jipthechip.diabolism.effect;

import jipthechip.diabolism.mixin.ChatHudAccessor;
import jipthechip.diabolism.mixin.GoalSelectorAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WardenEntity;

public abstract class IncapacitatedStatusEffect extends ClientSyncedStatusEffect{

    protected IncapacitatedStatusEffect(StatusEffectCategory category, int color, String registryName) {
        super(category, color, registryName);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setVelocity(0, entity.getVelocity().getY(), 0);
        if(entity.getX() != entity.prevX || entity.getZ() != entity.prevZ) {
            entity.setPosition(entity.prevX, entity.getY(), entity.prevZ);
        }

        if((entity instanceof MobEntity mobEntity) && ! entity.getWorld().isClient)
        {
            GoalSelector goalSelector = ((GoalSelectorAccessor)mobEntity).getGoalSelector();

            goalSelector.disableControl(Goal.Control.LOOK);
            goalSelector.disableControl(Goal.Control.JUMP);
            goalSelector.disableControl(Goal.Control.MOVE);
            goalSelector.disableControl(Goal.Control.TARGET);
        }

        else if(entity.getWorld().isClient){
            MinecraftClient client = MinecraftClient.getInstance();
            ChatHud chatHud = client.inGameHud.getChatHud();
            if(entity == client.player && chatHud != null &&  !((ChatHudAccessor)chatHud).isChatFocusedInvoker()) {
                KeyBinding.setKeyPressed(InputUtil.fromKeyCode(InputUtil.GLFW_KEY_W,0), false);
                KeyBinding.setKeyPressed(InputUtil.fromKeyCode(InputUtil.GLFW_KEY_A,0), false);
                KeyBinding.setKeyPressed(InputUtil.fromKeyCode(InputUtil.GLFW_KEY_S,0), false);
                KeyBinding.setKeyPressed(InputUtil.fromKeyCode(InputUtil.GLFW_KEY_D,0), false);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
