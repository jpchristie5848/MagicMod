package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.MagicElementColors;
import jipthechip.diabolism.data.MagickaCrystal;
import jipthechip.diabolism.data.spell.Spell;
import jipthechip.diabolism.effect.DiabolismEffects;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SpellGemTextureProvider implements ItemLayerTextureProvider{

    private static final SpriteIdentifier SPELL_GEM_BASE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_gem_0"));

    @Override
    public List<ItemLayerTexture> getTextures(ItemStack stack) {
        List<ItemLayerTexture> textures = new ArrayList<>();

        Spell spell = DataUtils.readObjectFromItemNbt(stack, Spell.class);

        if(spell != null){
            int color = DiabolismEffects.MAP.get(spell.getEffects().get(0).getEffectKey()).getColor();
            color = color | 0xff000000;

            textures.add(new ItemLayerTexture(SPELL_GEM_BASE.getSprite(), color, 0));
        }
        return textures;
    }
}
