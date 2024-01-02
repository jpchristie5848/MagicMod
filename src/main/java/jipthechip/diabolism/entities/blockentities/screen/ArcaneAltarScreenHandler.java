package jipthechip.diabolism.entities.blockentities.screen;

import jipthechip.diabolism.data.MagickaFluid;
import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class ArcaneAltarScreenHandler extends AbstractRecipeScreenHandler<Inventory> {

    private final Inventory inventory;

    private final ArcaneAltar altar;

    private BlockPos pos;

    public ArcaneAltarScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {

        this(syncId, playerInventory, new SimpleInventory(4), (ArcaneAltar) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
        //this.pos = buf.readBlockPos();

    }

    public ArcaneAltarScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ArcaneAltar altar) {
        super(DiabolismScreens.ARCANE_ALTAR_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        //this.propertyDelegate = delegate;
        this.altar = altar;

        inventory.onOpen(playerInventory.player);

        int row;
        int col;

        this.addSlot(new IngredientSlot(inventory, 0, 56, 16, DiabolismItems.MAGICKA_CRYSTAL));
        this.addSlot(new IngredientSlot(inventory, 1, 8, 116, DiabolismItems.SPELL_TEMPLATE));
        this.addSlot(new IngredientSlot(inventory, 2, 109, 116, DiabolismItems.SPELL_MODIFIER));
        this.addSlot(new OutputSlot(inventory, 3, 148, 77));

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 158 + row * 18));
            }
        }
        //The player Hotbar
        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 216));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        System.out.println("quickmove inv slot: "+invSlot);
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            System.out.println("inventory size: "+this.inventory.size());
            System.out.println("slots size: "+this.slots.size());
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public boolean isCrafting() {
        System.out.println("progress : "+altar.getProgress());
        return altar.getProgress() > 0;
    }

    public int getScaledProgress() {
        int progress = altar.getProgress();
        int maxProgress = altar.getMaxProgress();
        int progressArrowSize = 22;

        int scaledProgress = maxProgress != 0 && progress != 0 ? (progress * progressArrowSize) / maxProgress : 0;
        System.out.println("scaled progress: "+scaledProgress);

        return scaledProgress;
    }

    public List<MagickaFluid> getMagickaFluids(){


        if(this.altar == null) {
            return Collections.emptyList();
        }
        return this.altar.getFluids();
    }

    public float getRemainingSpace(){
        return this.altar.getRemainingSpace();
    }

    public int getContainerCapacity(){
        if (this.altar == null) {
            return 0;
        }
        return this.altar.getCapacity();
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        if(!fromLast && endIndex >= this.inventory.size()){
            endIndex =  this.inventory.size()-1; // stops items from getting inserted into output slot
        }
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.getIndex() != 3 && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        return slot.getIndex() != 3 && super.canInsertIntoSlot(slot);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }


    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {

    }

    @Override
    public void clearCraftingSlots() {

    }

    @Override
    public boolean matches(RecipeEntry<? extends Recipe<Inventory>> recipe) {
        return false;
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 3;
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 4;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        System.out.println("can insert slot index: "+index);
        return index != 3;
    }
}
