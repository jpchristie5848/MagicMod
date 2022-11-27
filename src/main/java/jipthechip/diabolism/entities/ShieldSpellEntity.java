package jipthechip.diabolism.entities;

import jipthechip.diabolism.Utils.MathUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ShieldSpellEntity extends Entity{

    int playerEntityId;

    public ShieldSpellEntity(EntityType<ShieldSpellEntity> type, World world) {
        super(type, world);
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
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public void setPlayerEntityId(int playerEntityId) {
        this.playerEntityId = playerEntityId;
    }

    @Override
    public void tick() {
        super.tick();

        System.out.println("Attempting to get player with entityid: "+playerEntityId);
        Entity playerEntity = this.world.getEntityById(playerEntityId);

        if(playerEntity == null){
            System.out.println("player entity not found, killing shield spell entity");
            this.kill();
            return;
        }

        Vec3d lookVector = MathUtils.PitchAndYawToVec3d(playerEntity.getPitch(), playerEntity.getYaw()*-1);

        Vec3d newPos = playerEntity.getPos().add(lookVector.multiply(-2));

        setPos(newPos.getX(), newPos.getY()+1, newPos.getZ());

        Vec3d leftVector = MathUtils.getPerpendicularToLookVector(lookVector);
        Vec3d rightVector = leftVector.multiply(-1);
        Vec3d downVector = leftVector.crossProduct(lookVector);
        Vec3d upVector = downVector.multiply(-1);

        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                ParticleTypes.ELECTRIC_SPARK, true, leftVector.x+upVector.x+newPos.x, leftVector.y+upVector.y+newPos.y+1, leftVector.z+upVector.z+newPos.z, 1,
                0, 0, 0, 0));

        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                ParticleTypes.ELECTRIC_SPARK, true, leftVector.x+downVector.x+newPos.x, leftVector.y+downVector.y+newPos.y+1, leftVector.z+downVector.z+newPos.z, 1,
                0, 0, 0, 0));

        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                ParticleTypes.ELECTRIC_SPARK, true, rightVector.x+upVector.x+newPos.x, rightVector.y+upVector.y+newPos.y+1, rightVector.z+upVector.z+newPos.z, 1,
                0, 0, 0, 0));

        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                ParticleTypes.ELECTRIC_SPARK, true, rightVector.x+downVector.x+newPos.x, rightVector.y+downVector.y+newPos.y+1, rightVector.z+downVector.z+newPos.z, 1,
                0, 0, 0, 0));

        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                ParticleTypes.ELECTRIC_SPARK, true, newPos.x, newPos.y+1, newPos.z, 1,
                0, 0, 0, 0));

        Box finalBox = this.calculateBoundingBox();
        this.setBoundingBox(finalBox);
        PlayerLookup.tracking(this).forEach(player -> {
            ((ServerWorld) world).spawnParticles(player,
                    ParticleTypes.FLAME, true, finalBox.minX, finalBox.minY, finalBox.minZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.maxY, finalBox.maxZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.maxY, finalBox.minZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.minY, finalBox.minZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.minY, finalBox.maxZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.maxY, finalBox.maxZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.minY, finalBox.maxZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.maxY, finalBox.minZ, 1,
                    0, 0, 0, 0);
            ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, this.getPos().x, this.getPos().y, this.getPos().z, 1,
                    0, 0, 0, 0);
        });
    }

    public Box calculateBoundingBox() {
        float f = 0.5f;
        return new Box(this.getX() - (double)f, this.getY()-f, this.getZ() - (double)f, this.getX() + (double)f, this.getY() + f, this.getZ() + (double)f);
    }

    public Vec3d getPlayerLookVector(){
        Entity player = getWorld().getEntityById(playerEntityId);
        return MathUtils.PitchAndYawToVec3d(player.getPitch(), player.getYaw()*-1);
    }

    public boolean isCollidable(){
        return true;
    }

    public boolean canHit(){
        return true;
    }
}
