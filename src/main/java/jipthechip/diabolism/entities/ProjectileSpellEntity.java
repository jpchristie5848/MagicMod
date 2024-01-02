package jipthechip.diabolism.entities;


import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.Spell;
import jipthechip.diabolism.data.StatusEffectInstanceContainer;
import jipthechip.diabolism.mixin.EntityAccessor;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.potion.AbstractElementalStatusEffect;
import jipthechip.diabolism.potion.ClientSyncedStatusEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectileSpellEntity extends PersistentProjectileEntity {

    private float radius;
    private Vec3d velocity;

    private List<StatusEffectInstance> spellEffects;

    private void init(){
        setNoGravity(true);
        setDamage(10.0);

        //System.out.println("CONSTRUCTOR setting dimensions to: "+EntityDimensions.fixed(radius, radius));
        ((EntityAccessor)this).setDimensions(EntityDimensions.fixed(getRadius()*1.2f, getRadius()*1.2f));
    }
    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        init();
    }

    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, Vec3d velocity, StatusEffectInstance... spellEffects) {
        super(type, x, y, z, world);
        this.spellEffects = Arrays.stream(spellEffects).toList();
        this.velocity = velocity;
        init();
    }

    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
        init();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int NumEffects = nbt.getInt("NumEffects");
        spellEffects = new ArrayList<>();
        for(int i = 0; i < NumEffects; i++){
            spellEffects.add(((StatusEffectInstanceContainer)DataUtils.DeserializeFromString(nbt.getString("SpellEffect"+i))).createInstance());
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("NumEffects", spellEffects.size());
        for(int i = 0; i < spellEffects.size(); i++){
            nbt.putString("SpellEffect"+i,DataUtils.SerializeToString(new StatusEffectInstanceContainer(spellEffects.get(i))));
        }
        return super.writeNbt(nbt);
    }

    public void tick(){

//        System.out.println("Bounding box size in tick: "+MathUtils.distanceBetween2Points(getBoundingBox().minX, getBoundingBox().minY, getBoundingBox().minZ, getBoundingBox().maxX, getBoundingBox().maxY, getBoundingBox().maxZ));

        ((EntityAccessor)this).setDimensions(EntityDimensions.fixed(getRadius()*1.2f, getRadius()*1.2f));
        //System.out.println("before super tick: "+this.getBoundingBox());

        super.tick();

        //System.out.println("after super tick: "+this.getBoundingBox());

        if(!getWorld().isClient()) {

            if(this.isTouchingWater() || this.isInLava()){
                this.kill();
                return;
            }

            if(this.velocity != null) {
                super.setVelocity(this.velocity);
                //System.out.println("Setting velocity to: "+this.velocity);
            }

//            if(!synchedWithClient){
//                PacketByteBuf buf = PacketByteBufs.create();
//                buf.writeInt(getId());
//                buf.writeFloat(radius);
//                PlayerLookup.tracking(this).forEach(player -> {
//                    ServerPlayNetworking.send(player, DiabolismPackets.SET_ENTITY_RADIUS_PACKET, buf);
//                    System.out.println("sent packet to player "+player.getUuidAsString()+": "+getId()+"|"+radius);
//                });
//                synchedWithClient = true;
//            }

            playParticles();


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

    private void playParticles() {
        System.out.println("start of playParticles");

        float radius = getRadius();

        int LevelsHorizontal = (int) (2.0f * radius * 20.0f);
        int LevelsVertical = (int) (2.0f * radius * 20.0f);

        Box boundingBox = getBoundingBox();

        double halfDeltaX = (boundingBox.maxX-boundingBox.minX)/2;
        double halfDeltaY = (boundingBox.maxY-boundingBox.minY)/2;
        double halfDeltaZ = (boundingBox.maxZ-boundingBox.minZ)/2;

        Vec3d boundingBoxCenter = new Vec3d(boundingBox.maxX - halfDeltaX, boundingBox.maxY - halfDeltaY, boundingBox.maxZ - halfDeltaZ);

        // spawn particles so entity resembles a sphere
        for(int i = 0; i < LevelsVertical; i++){
            for(int j = 0; j < LevelsHorizontal; j++){

                System.out.println("spawning particle");
                Vec3d position = MathUtils.getPointOnSphere(((float)i/(float)LevelsVertical)*180.0f-90.0f, ((float)j/(float)LevelsHorizontal)*360.0f, radius, boundingBoxCenter);
                PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) getWorld()).spawnParticles(player,
                        ColoredSpellParticleFactory.createData(getParticleColor(), (int) (Math.random() * 25 + 10)), true, position.getX(), position.getY(), position.getZ(), 1,
                        0, 0, 0, 0));

            }
        }
    }

    private int getParticleColor(){

        List<Float> probabilities = new ArrayList<>();
        float probabilitiesTotal = 0;

        for(StatusEffectInstance instance : spellEffects){
            float probability = (instance.getAmplifier() + ((float)instance.getDuration() / 6))/2;
            probabilities.add(probability);
            probabilitiesTotal += probability;
        }

        for (int i = 0; i < probabilities.size(); i++){
            probabilities.set(i, probabilities.get(i)/probabilitiesTotal);
        }

        double random = Math.random();
        float sum = 0;
        int chosenEffectIndex = 0;

        for (int i = 0; i < probabilities.size(); i++){
            sum += probabilities.get(i);
            if(random < sum){
                chosenEffectIndex = i;
                break;
            }
        }

        MagicElement effectElement = ((AbstractElementalStatusEffect)spellEffects.get(chosenEffectIndex).getEffectType()).getElement();
        // get color for chosen spell effect
        return Spell.ELEMENT_COLORS[effectElement.ordinal()];

    }

    private float getRadius(){

        if(spellEffects == null){
            return 0.2f;
        }

        float radius = 0;

        for(StatusEffectInstance instance : spellEffects){
            float effectTotal = (instance.getAmplifier() + ((float)instance.getDuration() / 6))/2;
            radius += (effectTotal / 100)/2;
        }
        return Math.max(0.1f, radius);
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


        if(entityHitResult.getEntity() instanceof LivingEntity target){
            target.timeUntilRegen = 0;
            if(spellEffects != null && !spellEffects.isEmpty()){
                for(StatusEffectInstance instance : spellEffects){
                    target.addStatusEffect(instance);
                }
            }
        }

    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if(!getWorld().isClient && (inGround || isNoClip()) && shake <= 0)
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
    public Box calculateBoundingBox() {
        float f = this.getRadius()*0.6f;
        return new Box(this.getX() - (double)f, this.getY()-f, this.getZ() - (double)f, this.getX() + (double)f, this.getY() + f, this.getZ() + (double)f);
    }

    public void setRadius(float radius){
        this.radius = radius;
    }


    public void setRealVelocity(Vec3d velocity){
        this.velocity = velocity;
        super.setVelocity(velocity);
        //System.out.println("set velocity to "+velocity+" in set real velocity");
    }

    public boolean isCollidable(){
        return true;
    }
}
