package jipthechip.diabolism.entities.blockentities;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagickaCrystal;
import jipthechip.diabolism.data.MagickaFluid;
import jipthechip.diabolism.data.brewing.entity.ArcaneAltarData;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.data.spell.SpellTemplate;
import jipthechip.diabolism.data.spell.SpellType;
import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.screen.blockentity.ArcaneAltarScreenHandler;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.packets.StatusEffectInstanceData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Objects;

public class ArcaneAltar extends AbstractMagickaConsumer<ArcaneAltarData> implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("arcane_altar_idle");
    private static final RawAnimation ACTIVE_ANIM = RawAnimation.begin().thenLoop("arcane_altar_active");

    public ArcaneAltar(BlockPos pos, BlockState state) {
        super(DiabolismEntities.ARCANE_ALTAR_BLOCKENTITY, pos, state, 1000, new ArcaneAltarData());
    }

    public static void ticker(World world, BlockPos pos, BlockState state, ArcaneAltar be) {
        be.tick(world, pos, state);
    }

    private void tick(World world, BlockPos pos, BlockState state) {

        if(!world.isClient && canUpdate()){
            incrementProgress();
            markDirty();
            sync();
        }

    }

    protected void incrementProgress(){
        data.setProgress(Math.max(0, Math.min(maxProgress, data.getProgress() + (hasRecipe() ? 1 : -1))));
        lastUpdate = System.currentTimeMillis();
        if(data.getProgress() >= maxProgress){
            craft();
        }
    }

    protected boolean canUpdate(){
        return System.currentTimeMillis() - lastUpdate >= 100;
    }

    protected void craft(){
        if(hasRecipe()){
            ItemStack spellGlyphStack = new ItemStack(DiabolismItems.SPELL_GEM);

            MagicElement element = Objects.requireNonNull(DataUtils.readObjectFromItemNbt(inventory.get(0), MagickaCrystal.class)).getElement();
            SpellType spellType = Objects.requireNonNull(DataUtils.readObjectFromItemNbt(inventory.get(1), SpellTemplate.class)).getSpellType();
            StatusEffectInstanceData effectData = DiabolismEffects.getEffectForSpell(element, this);

            Spell spell = new Spell(spellType, effectData);
            DataUtils.writeObjectToItemNbt(spellGlyphStack, spell);

            inventory.set(3, spellGlyphStack);
            data.setProgress(0);
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ArcaneAltarScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        sync();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ArcaneAltar> controller = new AnimationController<ArcaneAltar>(this, "arcane_altar_controller", this::predicate);
        controllers.add(controller);
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event){
        if(isCrafting()){
            event.setAnimation(ACTIVE_ANIM);
        }else{
            event.setAnimation(IDLE_ANIM);
        }
        return PlayState.CONTINUE;
    }

    public boolean isCrafting(){
        return hasRecipe() && data.getProgress() <= maxProgress;
    }

    public float getScaledMagicElement(MagicElement element){
        for(MagickaFluid magickaFluid : data.getFluids()){
            if(magickaFluid.getElement() == element && magickaFluid.getAmount() > 0){
                return MathUtils.roundToDecimalPlace((magickaFluid.getAmount()*magickaFluid.getPurity())/capacity,4);
            }
        }
        return 0.0f;
    }

    private boolean hasRecipe(){
        return inventory.get(0).getItem() == DiabolismItems.MAGICKA_CRYSTAL
                && inventory.get(1).getItem() == DiabolismItems.SPELL_TEMPLATE
                && inventory.get(2).getItem() == DiabolismItems.SPELL_MODIFIER
                && inventory.get(3).isEmpty()
                && getScaledMagicElement(Objects.requireNonNull(DataUtils.readObjectFromItemNbt(inventory.get(0), MagickaCrystal.class)).getElement())>0.0f;
    }

//    @Override
//    public void syncInventoryWithClient() {
//        PacketUtils.syncBlockInventoryWithClient(getWorld(), getPos(), this);
//    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }
}
