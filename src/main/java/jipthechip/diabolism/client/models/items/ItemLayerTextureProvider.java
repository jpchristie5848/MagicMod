package jipthechip.diabolism.client.models.items;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ItemLayerTextureProvider {

    public List<ItemLayerTexture> getTextures(ItemStack stack);
}
