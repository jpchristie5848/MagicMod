package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.brewing.Fluid;
import jipthechip.diabolism.data.brewing.entity.FermenterData;
import jipthechip.diabolism.data.brewing.Yeast;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class MagicFermenter extends AbstractFluidContainer<FermenterData> implements GeoBlockEntity {

    private long tickCounter = 0;
    private long lastToggled = 0;

    private long lastFermentUpdate = 0;
    private boolean fermentationOngoing = false;

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenLoop("lid_open");
    private static final RawAnimation FLOAT_ANIM = RawAnimation.begin().thenLoop("lid_float");

    private static final RawAnimation[] FERMENT_ANIMS = new RawAnimation[]{RawAnimation.begin().thenPlay("lid_ferment0"),
            RawAnimation.begin().thenPlay("lid_ferment1"),
            RawAnimation.begin().thenPlay("lid_ferment2"),
            RawAnimation.begin().thenPlay("lid_ferment3")
    };

    private AnimationController<MagicFermenter>[] fermentControllers = new AnimationController[]{
            new AnimationController<MagicFermenter>(this, "ferment0", state -> PlayState.STOP).triggerableAnim("lid_ferment0", FERMENT_ANIMS[0]),
            new AnimationController<MagicFermenter>(this, "ferment1", state -> PlayState.STOP).triggerableAnim("lid_ferment1", FERMENT_ANIMS[1]),
            new AnimationController<MagicFermenter>(this, "ferment2", state -> PlayState.STOP).triggerableAnim("lid_ferment2", FERMENT_ANIMS[2]),
            new AnimationController<MagicFermenter>(this, "ferment3", state -> PlayState.STOP).triggerableAnim("lid_ferment3", FERMENT_ANIMS[3])
    };

    public MagicFermenter(BlockPos pos, BlockState state) {
        super(DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, pos, state, 1000, true, new FermenterData());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //controllerRegistrar.add()
        controllerRegistrar.add(new AnimationController<MagicFermenter>(this, "magic_fermenter_controller", this::predicate));

        for(AnimationController<MagicFermenter> controller : fermentControllers){
            controllerRegistrar.add(controller);
        }
    }

    private PlayState predicate(AnimationState<MagicFermenter> animationState) {

        if(data != null && data.getExtendedData().isOpen()){
            if (System.currentTimeMillis() - lastToggled < 1000){
                animationState.setAnimation(OPEN_ANIM);
            }else{
                animationState.setAnimation(FLOAT_ANIM);
            }
        }
        else{
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    @Override
    public double getBoneResetTime() {
        return 20;
    }

    public static void ticker(World world, BlockPos blockPos, BlockState state, MagicFermenter be) {
        be.tick(world, blockPos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState state){
        tickCounter++;

        // only start fermentation when container is closed, full of fluid, and yeast is added
        if(data != null && !this.data.getExtendedData().isOpen() && data.isFull(this.capacity) && this.data.getExtendedData().getYeast() != null && !world.isClient) {

            if(System.currentTimeMillis() - lastFermentUpdate > 200){

                fermentationOngoing = false;
                HashMap<MagicElement, Float> yieldMultipliers = this.data.getExtendedData().getYeast().getYieldMultipliers();

                for(MagicElement element : data.getElementContentsMap().keySet()){
                    float existingElementContent = data.getElementContentFromMap(element);
                    if(existingElementContent > 0){
                        double elementToRemove = Math.min(0.25f, existingElementContent);
                        double magickaToCreate = (yieldMultipliers.get(element)*elementToRemove);
                        if(existingElementContent <= 0.25f){
                            data.setElementContents(element, 0.0f);
                            data.setMagickaContents(element, MathUtils.roundToDecimalPlace(data.getMagickaContentFromMap(element)+magickaToCreate,4));
                        }else{
                            data.removeElementContent(element, (float) elementToRemove);
                            data.addMagickaContent(element, (float) magickaToCreate);
                        }
                        fermentationOngoing = true;
                    }
                }
                // destroy yeast when fermentation is completed
                if(!fermentationOngoing){
                    this.data.getExtendedData().setYeast(null);
                }
                System.out.println("current elements: "+ DataUtils.getMapString(data.getElementContents()));
                System.out.println("current magicka: "+ DataUtils.getMapString(data.getMagickaContents()));

                sync();
                markDirty();
                lastFermentUpdate = System.currentTimeMillis();

                // trigger animation
                int animIndex = (new Random()).nextInt(4);

                this.triggerAnim("ferment"+animIndex, "lid_ferment"+animIndex);
            }
            // spawn fermentation particles
            if (tickCounter % 10 == 0){
                spawnFermentationParticles();
            }

        }

        // spawn magicka particles when container is open and fluid has magicka
        if(tickCounter % 5 == 0){
            spawnMagickaParticles();
        }
    }

    private void spawnMagickaParticles(){
        if(data != null && this.data.getExtendedData().isOpen() && data.hasMagickaContent() && !world.isClient){
            double randomX = (Math.random()*0.7);
            double randomY = (Math.random()*0.05);
            double randomZ  = (Math.random()*0.7);
            Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+0.9+randomY, pos.getZ()+0.15 + randomZ);
            int age = (int) (Math.random()*20+5.0f);
            int color = data.getMagickaParticleColor();
            PlayerLookup.tracking(this).forEach(player ->
                    ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
        }
    }

    private void spawnFermentationParticles(){
        if(this.data.getExtendedData().getYeast() != null && this.data.hasElementContent() && !world.isClient){
            double randomX = (Math.random()*0.7);
            double randomY = (Math.random()*0.05);
            double randomZ  = (Math.random()*0.7);
            Vec3d sparklePos = new Vec3d(pos.getX()+0.15 + randomX, pos.getY()+1.2+randomY, pos.getZ()+0.15 + randomZ);
            int age = (int) (Math.random()*10+30);
            Random random = new Random();
            int i = random.nextInt(3);
            int color = i == 0 ? getFluidColor() : this.data.getExtendedData().getYeast().getColor(i-1);
            PlayerLookup.tracking(this).forEach(player ->
                    ((ServerWorld)world).spawnParticles(ColoredSpellParticleFactory.createData(color, age), sparklePos.getX(), sparklePos.getY(), sparklePos.getZ(), 1, 0.01,0.25, 0.01, 0));
        }
    }

    public void toggleOpen(){
        if(System.currentTimeMillis() - this.lastToggled > 1000){
            this.lastToggled = System.currentTimeMillis();
            this.data.getExtendedData().toggleOpen();
            this.markDirty();
            this.sync();
        }
    }

    public boolean fillBucket(PlayerEntity player, Hand hand){

        boolean bucketFilled = super.fillBucket(player, hand);
        if(bucketFilled) {
            this.data.getExtendedData().setYeast(null);
            markDirty();
            sync();
        }
        return bucketFilled;
    }

    public boolean addYeastItem(ItemStack stack, Hand hand, PlayerEntity player){

        if(world != null && !world.isClient && stack.getItem() == DiabolismItems.MYSTICAL_YEAST && this.getFluidData() != null && canInteract()){
            lastInteractionTime = System.currentTimeMillis();
            Yeast yeast = DataUtils.readObjectFromItemNbt(stack, Yeast.class);
            if(yeast != null){
                this.data.getExtendedData().setYeast(yeast);
                player.setStackInHand(hand, ItemStack.EMPTY);

                lastInteractionTime = System.currentTimeMillis();
                markDirty();
                sync();
                return true;
            }
        }
        return false;
    }
}
