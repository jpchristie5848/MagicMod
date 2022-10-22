package jipthechip.diabolism.entities;


import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.mixin.EntityAccessor;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileSpellEntity extends PersistentProjectileEntity {

    private float radius;
    private Vec3d velocity;
    private boolean radiusSynchronized = false;

    private void construct(){
        setNoGravity(true);
        setDamage(10.0);
        if(!getEntityWorld().isClient) {
            radius = (float) ((Math.random() * (5.0 - 0.5)) + 0.5);
        }
        System.out.println("CONSTRUCTOR setting dimensions to: "+EntityDimensions.fixed(radius *2, radius *2));
        ((EntityAccessor)this).setDimensions(EntityDimensions.fixed(radius *2, radius *2));
    }
    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        construct();
    }

    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, Vec3d velocity) {
        super(type, x, y, z, world);
        construct();
        this.velocity = velocity;
    }

    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
        construct();
    }

    public void tick(){

        ((EntityAccessor)this).setDimensions(EntityDimensions.fixed(radius *2, radius *2));
        //System.out.println("before super tick: "+this.getBoundingBox());

        super.tick();

        //System.out.println("after super tick: "+this.getBoundingBox());

        if(!world.isClient()) {

            if(this.isTouchingWater() || this.isInLava()){
                this.kill();
                return;
            }

            if(this.velocity != null) {
                this.setVelocity(this.velocity);
            }

            if(!radiusSynchronized){
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(getId());
                buf.writeFloat(radius);
                PlayerLookup.tracking(this).forEach(player -> {
                    ServerPlayNetworking.send(player, DiabolismPackets.SET_ENTITY_RADIUS_PACKET, buf);
                    System.out.println("sent packet to player "+player.getUuidAsString()+": "+getId()+"|"+radius);
                });
                radiusSynchronized = true;
            }

            int LevelsHorizontal = (int)(Math.log(radius + 1.0)*25);
            int LevelsVertical = (int)(Math.log(radius + 1.0)*25);

            // spawn particles so entity resembles a sphere
            for(int i = 0; i < LevelsVertical; i++){
                for(int j = 0; j < LevelsHorizontal; j++){

                    Vec3d position = MathUtils.getPointOnSphere(((float)i/(float)LevelsVertical)*180.0f-90.0f, ((float)j/(float)LevelsHorizontal)*360.0f, radius, this.getPos());

                    PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                            ParticleTypes.ELECTRIC_SPARK, true, position.getX(), position.getY(), position.getZ(), 1,
                            0, 0, 0, 0));
                }
            }


//            for(int count = 0; count < 16; count++) {
//                double x = getX() + (world.random.nextInt(3) - 1) / 4D;
//                double y = getY() + 0.2F + (world.random.nextInt(3) - 1) / 4D;
//                double z = getZ() + (world.random.nextInt(3) - 1) / 4D;
//                double deltaX = (world.random.nextInt(3) - 1) * world.random.nextDouble();
//                double deltaY = (world.random.nextInt(3) - 1) * world.random.nextDouble();
//                double deltaZ = (world.random.nextInt(3) - 1) * world.random.nextDouble();
//
//                PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player, ParticleTypes.ELECTRIC_SPARK, true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
//            }
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        kill();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        if(entityHitResult.getEntity() instanceof LivingEntity target)
            target.timeUntilRegen = 0;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if(!world.isClient && (inGround || isNoClip()) && shake <= 0)
            discard();
    }

    @Override
    public ItemStack asItemStack() {
        return null;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.radius, this.radius);
    }

    @Override
    protected Box calculateBoundingBox() {
        float f = this.radius / 2.0F;
        float g = this.radius;
        return new Box(this.getX() - (double)f, this.getY(), this.getZ() - (double)f, this.getX() + (double)f, this.getY() + (double)g, this.getZ() + (double)f);
    }

    public void setRadius(float radius){
        this.radius = radius;
    }

}
