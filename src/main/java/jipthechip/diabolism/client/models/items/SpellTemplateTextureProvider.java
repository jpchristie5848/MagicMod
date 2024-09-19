package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.SpellTemplate;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SpellTemplateTextureProvider implements ItemLayerTextureProvider{

    private static final SpriteIdentifier BASE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_template_base"));
    private static final SpriteIdentifier PROJECTILE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_template_overlay_projectile"));
    private static final SpriteIdentifier SELF_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_template_overlay_self"));


    @Override
    public List<ItemLayerTexture> getTextures(ItemStack stack) {

        List<ItemLayerTexture> textures = new ArrayList<>();
        textures.add(new ItemLayerTexture(BASE_ID.getSprite(), -1, 0));
        NbtCompound nbt = stack.getNbt();

        if(nbt != null){
            SpellTemplate spellTemplate = DataUtils.readObjectFromItemNbt(stack, SpellTemplate.class);
            if(spellTemplate != null){
                switch (spellTemplate.getSpellType()) {
                    case SELF -> textures.add(new ItemLayerTexture(SELF_ID.getSprite(), -1, 1));
                    case PROJECTILE -> textures.add(new ItemLayerTexture(PROJECTILE_ID.getSprite(), -1, 1));
                }
            }
        }

        return textures;
    }
}
