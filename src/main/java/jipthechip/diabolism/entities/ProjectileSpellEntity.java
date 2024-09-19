package jipthechip.diabolism.entities;


import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.mixin.EntityAccessor;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.effect.AbstractElementalStatusEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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

    private List<StatusEffectInstanceData> spellEffects;

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

    public ProjectileSpellEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, Vec3d velocity, StatusEffectInstanceData... spellEffects) {
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
            spellEffects.add(((StatusEffectInstanceData)DataUtils.DeserializeFromString(nbt.getString("SpellEffect"+i))));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("NumEffects", spellEffects.size());
        for(int i = 0; i < spellEffects.size(); i++){
            nbt.putString("SpellEffect"+i,DataUtils.SerializeToString(spellEffects.get(i)));
        }
        return super.writeNbt(nbt);
    }

    public void tick(){

//        System.out.println("Bounding box size in tick: "+MathUtils.distanceBetween2Points(getBoundingBox().minX, getBoundingBox().minY, getBoundingBox().minZ, getBoundingBox().maxX, getBoundingBox().maxY, getBoundingBox().maxZ));

        ((EntityAccessor)this).setDimensions(EntityDimensions.fixed(getRadius()*1.2f, getRadius()*1.2f));
        //System.out.println("before super tick: "+this.getBoundingBox());

        super.tick();

        if(!getWorld().isClient()) {

            if(this.isTouchingWater() || this.isInLava()){
                this.kill();
                return;
            }

            if(this.velocity != null) {
                super.setVelocity(this.velocity);
            }

            playParticles();
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

        for(StatusEffectInstanceData data : spellEffects){
            float probability = (data.getAmplifier() + ((float)data.getDuration() / 6))/2;
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
        String effectKey = spellEffects.get(chosenEffectIndex).getEffectKey();
        if(DiabolismEffects.MAP.get(effectKey) instanceof AbstractElementalStatusEffect elementalEffect){
            MagicElement effectElement = elementalEffect.getElement();

            // get color for chosen spell effect
            return Spell.ELEMENT_COLORS[effectElement.ordinal()];
        }

        return 0;

    }

    private float getRadius(){

        if(spellEffects == null){
            return 0.2f;
        }

        float radius = 0;

        for(StatusEffectInstanceData data : spellEffects){
            float effectTotal = (data.getAmplifier() + ((float)data.getDuration() / 6))/2;
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
                for(StatusEffectInstanceData data : spellEffects){
                    target.addStatusEffect(data.createInstance());
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
