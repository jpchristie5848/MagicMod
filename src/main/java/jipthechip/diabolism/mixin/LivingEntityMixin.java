package jipthechip.diabolism.mixin;

import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.ShieldSpellEntity;
import jipthechip.diabolism.packets.DiabolismPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IMagicProperties {

    private static final TrackedData<Integer> MagicShieldEntityId = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> Awakened = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private int MagickaCounter = 1;

    private static final TrackedData<Integer> Magicka = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Float> MagickaRegenRate = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final TrackedData<Integer> MaxMagicka = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void diabolism$tick(CallbackInfo ci){
        if(this.dataTracker.get(Awakened)){
            MagickaCounter++;
            if(getMagicka() > getMaxMagicka()){
                this.dataTracker.set(Magicka, getMaxMagicka());
            }
            if(MagickaCounter % (100 * this.dataTracker.get(MagickaRegenRate)) == 0 && getMagicka() < getMaxMagicka()){
                this.dataTracker.set(Magicka, this.dataTracker.get(Magicka) + 1);
                System.out.println("magicka was set to: "+this.dataTracker.get(Magicka));
            }
        }
    }

    @Inject(method = "initDataTracker()V", at = @At("TAIL"))
    private void initTrackedMagicData(CallbackInfo ci){
        this.dataTracker.startTracking(MagicShieldEntityId, -1);
        this.dataTracker.startTracking(Awakened, false);
        this.dataTracker.startTracking(Magicka, 0);
        this.dataTracker.startTracking(MagickaRegenRate, 0.0f);
        this.dataTracker.startTracking(MaxMagicka, 0);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"), locals= LocalCapture.CAPTURE_FAILHARD)
    private void writeToNBT(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("MagicShieldEntityId", this.getMagicShield());
        nbt.putBoolean("Awakened", this.isAwakened());
        nbt.putInt("Magicka", this.getMagicka());
        nbt.putFloat("MagickaRegenRate", this.getMagickaRegenRate());
        nbt.putInt("MaxMagicka", this.getMaxMagicka());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"), locals= LocalCapture.CAPTURE_FAILHARD)
    private void readFromNBT(NbtCompound nbt, CallbackInfo ci){
        this.setMagicShield(nbt.getInt("MagicShieldEntityId"));
        this.setAwakened(nbt.getBoolean("Awakened"));
        this.setMagicka(nbt.getInt("Magicka"));
        //System.out.println("Read magicka from nbt: "+nbt.getInt("Magicka"));
        this.setMagickaRegenRate(nbt.getFloat("MagickaRegenRate"));
        this.setMaxMagicka(nbt.getInt("MaxMagicka"));
    }

    @Override
    public int getMagicShield() {
        return this.dataTracker.get(MagicShieldEntityId);
    }

    public void setMagicShield(int entityId){
        this.dataTracker.set(MagicShieldEntityId, entityId);
    }

    public void toggleMagicShield(){

        int magicShieldEntityId = getMagicShield();

        if(magicShieldEntityId == -1){
            createMagicShield();
        }else{
            killMagicShield();
        }
    }

    public void deinitialize(){
        killMagicShield();
    }
    private void createMagicShield(){
        ShieldSpellEntity shieldSpellEntity = new ShieldSpellEntity(DiabolismEntities.SHIELD_SPELL, world);

        shieldSpellEntity.setPlayerEntityId(getId());
        setMagicShield(shieldSpellEntity.getId());

        Vec3d lookVector = MathUtils.PitchAndYawToVec3d(getPitch(), getYaw());

        shieldSpellEntity.setPos(getPos().x+lookVector.x, getPos().y+lookVector.y+1, getPos().z+lookVector.z);

        world.spawnEntity(shieldSpellEntity);
        System.out.println("Spawned magic shield entity with  id: "+shieldSpellEntity.getId());
    }
    private void killMagicShield(){
        int magicShieldEntityId = getMagicShield();
        if(magicShieldEntityId != -1){
            ShieldSpellEntity shieldSpellEntity = (ShieldSpellEntity) world.getEntityById(magicShieldEntityId);
            if(shieldSpellEntity != null){
                shieldSpellEntity.kill();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(magicShieldEntityId);

                ClientPlayNetworking.send(DiabolismPackets.KILL_ENTITY_PACKET, buf);

                System.out.println("Magic shield entity was not null");
            }
            setMagicShield(-1);
            System.out.println("Killed magic shield entity with id: "+magicShieldEntityId);
        }
    }

    public boolean isAwakened(){
        return this.dataTracker.get(Awakened);
    }

    public void setAwakened(boolean awakened){
        this.dataTracker.set(Awakened, awakened);
    }

    public int getMagicka(){
        return this.dataTracker.get(Magicka);
    }

    public void setMagicka(int magicka){
        this.dataTracker.set(Magicka, magicka);
    }

    public float getMagickaRegenRate(){
        return this.dataTracker.get(MagickaRegenRate);
    }

    public void setMagickaRegenRate(float regenRate){
        this.dataTracker.set(MagickaRegenRate, regenRate);
    }

    public int getMaxMagicka(){
        return this.dataTracker.get(MaxMagicka);
    }

    public void setMaxMagicka(int maxMagicka){
        this.dataTracker.set(MaxMagicka, maxMagicka);
    }
}
