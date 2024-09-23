package jipthechip.diabolism.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.Wand;
import jipthechip.diabolism.screen.blockentity.ArcaneAltarScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BasicWand extends Item {
    public BasicWand(Settings settings) {
        super(settings);
    }

}
