package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.MagicElement;
import jipthechip.diabolism.data.MagickaCrystal;
import jipthechip.diabolism.data.Spell;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MagickaCrystalTextureProvider implements ItemLayerTextureProvider{

    private static final SpriteIdentifier LAYER_0 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/magicka_crystal_0"));
    private static final SpriteIdentifier LAYER_1 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/magicka_crystal_1"));
    private static final SpriteIdentifier LAYER_2 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/magicka_crystal_2"));

    @Override
    public List<ItemLayerTexture> getTextures(ItemStack stack) {

        List<ItemLayerTexture> textures = new ArrayList<>();

        MagickaCrystal crystal = DataUtils.readObjectFromItemNbt(stack, MagickaCrystal.class);

        if(crystal != null){
            int tier = crystal.getTier();
            int color = Spell.ELEMENT_COLORS[crystal.getElement().ordinal()];

            if(tier >= 1){
                textures.add(new ItemLayerTexture(LAYER_0.getSprite(), color, 0));
                if(tier >= 2){
                    textures.add(new ItemLayerTexture(LAYER_1.getSprite(), color, 0));
                    if(tier >= 3){
                        textures.add(new ItemLayerTexture(LAYER_2.getSprite(), color, 1));
                    }
                }
            }
        }

        return textures;
    }
}
