package jipthechip.diabolism.entities.blockentities;

import io.wispforest.owo.util.ImplementedInventory;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.screen.ArcaneAltarScreenHandler;
import jipthechip.diabolism.items.DiabolismItems;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

public class ArcaneAltar extends AbstractMagickaConsumer implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("arcane_altar_idle");

    public ArcaneAltar(BlockPos pos, BlockState state) {
        super(DiabolismEntities.ARCANE_ALTAR_BLOCKENTITY, pos, state, 1000);

    }

    public static void ticker(World world, BlockPos pos, BlockState state, ArcaneAltar be) {
        be.tick(world, pos, state);
    }

    private void tick(World world, BlockPos pos, BlockState state) {

        if(System.currentTimeMillis() - lastUpdate >= 100 && inventory.get(3) == ItemStack.EMPTY && !world.isClient){
            progress = Math.max(0, Math.min(maxProgress, progress + (hasRecipe() ? 1 : -1)));
            lastUpdate = System.currentTimeMillis();

            if(progress >= maxProgress){
                inventory.set(3, new ItemStack(Items.STICK));
                progress = 0;
            }

            markDirty();
            syncWithClient();
        }

    }

    private void craft(){
        if(hasRecipe()){


            //inventory.set(3, )
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
        syncWithClient();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<ArcaneAltar> controller = new AnimationController<ArcaneAltar>(this, "arcane_altar_controller", this::predicate);

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.instanceCache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event){
        event.setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    private boolean hasRecipe(){
        return inventory.get(0).getItem() == DiabolismItems.MAGICKA_CRYSTAL && inventory.get(1).getItem() == DiabolismItems.SPELL_TEMPLATE && inventory.get(2).getItem() == DiabolismItems.SPELL_MODIFIER;
    }
}
