package jipthechip.diabolism.entities;

import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.particle.DiabolismParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WatcherEntity extends ParticleSpawningEntity {

    int playerEntityId;
    Vec3d relativePos;

    public WatcherEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public WatcherEntity(EntityType<WatcherEntity> watcher, double x, double y, double z, World world, int playerEntityId) {
        super(watcher, world);
        this.playerEntityId = playerEntityId;
        this.relativePos = new Vec3d(x, y, z);

        if(world.getEntityById(playerEntityId) instanceof PlayerEntity playerEntity){
            setPos(playerEntity.getPos().x + relativePos.x, playerEntity.getPos().y + relativePos.y, playerEntity.getPos().z + relativePos.z);
        }
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        playerEntityId = nbt.getInt("PlayerEntityId");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("PlayerEntityId", playerEntityId);
    }

    @Override
    public void tick() {
        super.tick();

        Entity entity = getWorld().getEntityById(playerEntityId);

        if(!(entity instanceof PlayerEntity playerEntity) || ((IMagicProperties)playerEntity).isAwakened() || playerEntity.getHealth() <= 0.0f) {
            this.kill();
        }else {

            setPos(playerEntity.getPos().x + relativePos.x, playerEntity.getPos().y + relativePos.y, playerEntity.getPos().z + relativePos.z);

            playParticles(playerEntity);
        }
    }

    @Override
    protected void playParticles(PlayerEntity playerEntity) {
        Vec3d lookVector = playerEntity.getPos().add(0,1,0).subtract(this.getPos()).normalize();
        Vec3d leftVector = MathUtils.getPerpendicularToLookVector(lookVector);

        ((ServerWorld) getWorld()).spawnParticles((ServerPlayerEntity) playerEntity, DiabolismParticles.WATCHER_PARTICLE, true,
                this.getPos().x + lookVector.x*0.5 + leftVector.x*0.25, this.getPos().y + lookVector.y*0.5 + leftVector.y*0.25,
                this.getPos().z + lookVector.z*0.5 + leftVector.z*0.25, 0,0,0,0,0);

        ((ServerWorld) getWorld()).spawnParticles((ServerPlayerEntity) playerEntity, DiabolismParticles.WATCHER_PARTICLE, true,
                this.getPos().x + lookVector.x*0.5 + leftVector.multiply(-1).x*0.25, this.getPos().y + lookVector.y*0.5 + leftVector.multiply(-1).y*0.25,
                this.getPos().z + lookVector.z*0.5 + leftVector.multiply(-1).z*0.25, 0,0,0,0,0);
    }
}