package jipthechip.diabolism.potion;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import jipthechip.diabolism.mixin.ChatHudAccessor;
import jipthechip.diabolism.mixin.GoalSelectorAccessor;
import jipthechip.diabolism.sound.DiabolismSounds;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.brain.task.WaitTask;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class FrozenStatusEffect extends ClientSyncedStatusEffect {

    protected FrozenStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
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

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        World world = entity.getWorld();
        if(!world.isClient){
            ((ServerWorld)world).playSoundFromEntity(null, entity, DiabolismSounds.MAGIC_FREEZE, SoundCategory.BLOCKS, 2.0f, 1.0f);
        }
        //entity.getWorld().playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
    }
//
//    @Override
//    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
//        super.onRemoved(entity, attributes, amplifier);
//        if(! (entity instanceof PlayerEntity) && ! entity.getWorld().isClient)
//        {
//            NbtCompound nbt = new NbtCompound();
//            nbt.putBoolean("NoAI", false);
//            entity.writeCustomDataToNbt(nbt);
//        }
//    }

    //    @Override
//    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
//        super.onRemoved(entity, attributes, amplifier);
//
//        // status effect sometimes not getting removed for some reason
//        if(entity.hasStatusEffect(DiabolismPotions.FROZEN_STATUS_EFFECT)){
//            entity.removeStatusEffect(DiabolismPotions.FROZEN_STATUS_EFFECT);
//        }
//    }
}
