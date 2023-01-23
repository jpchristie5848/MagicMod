package jipthechip.diabolism.items;

import jipthechip.diabolism.particle.DiabolismParticles;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.level.LevelProperties;

public class TotemOfThunderItem extends Item {


    public TotemOfThunderItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if(! world.isThundering() && world.getRegistryKey() == World.OVERWORLD){
            int damage = stack.getDamage();
            if (damage < stack.getMaxDamage() && ! player.isCreative()) {
                stack.setDamage(damage + 1);
            }

            if (! world.isClient){
                world.getServer().getWorld(World.OVERWORLD).setWeather(0, (int) (Math.random() * (179999 - 12000) + 12000), true, true);

                // spawn cosmetic lightning bolt
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightningEntity.setCosmetic(true);
                lightningEntity.setPos(player.getX(), player.getY(), player.getZ());
                world.spawnEntity(lightningEntity);

                // play spark particles
                playParticles(player, world);
            }
            return TypedActionResult.success(stack);
        }else{
            return TypedActionResult.pass(stack);
        }
    }

    private void playParticles(PlayerEntity player, World world){


        int numParticles = (int) (Math.random() * (40-20)+20);
        for(int i = 0; i < numParticles; i++){
            Vec3d particlePos = new Vec3d(player.getX()+(Math.random()*(2)-1), player.getY()+1+(Math.random()*(2)-1), player.getZ()+(Math.random()*(2)-1));
            Vec3d particleDelta = new Vec3d(particlePos.getX() - player.getX(), particlePos.getY() - (player.getY()+1), particlePos.getZ() - player.getZ()).normalize();

            PlayerLookup.tracking(player).forEach(otherplayer -> ((ServerWorld) world).spawnParticles(otherplayer,
                    ParticleTypes.ELECTRIC_SPARK, true, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1,
                    particleDelta.getX(), particleDelta.getY(), particleDelta.getZ(), Math.random() * 0.5));
            ((ServerWorld) world).spawnParticles((ServerPlayerEntity) player,
                    ParticleTypes.ELECTRIC_SPARK, true, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1,
                    particleDelta.getX(), particleDelta.getY(), particleDelta.getZ(), Math.random() * 0.5);
        }
    }
}
