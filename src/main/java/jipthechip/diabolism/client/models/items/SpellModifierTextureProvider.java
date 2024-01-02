package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.SpellModifier;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SpellModifierTextureProvider implements ItemLayerTextureProvider {


    private static final SpriteIdentifier BASE_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_base")); //"diabolism:item/spell_modifier_base"

    private static final SpriteIdentifier MULTI_1_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_multi_1"));
    private static final SpriteIdentifier MULTI_2_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_multi_2"));

    private static final SpriteIdentifier PRECISE_1_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_precise_1"));
    private static final SpriteIdentifier PRECISE_2_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_precise_2"));
    private static final SpriteIdentifier PRECISE_3_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_precise_3"));

    private static final SpriteIdentifier SPREAD_1_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_spread_1"));
    private static final SpriteIdentifier SPREAD_2_ID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/spell_modifier_spread_2"));

    private static final int BRONZE = 0xffbf850f;
    private static final int SILVER = 0xff97afc2;
    private static final int GOLD = 0xffffd000;
    private static final int BROWN = 0xff5e3c17;

    @Override
    public List<ItemLayerTexture> getTextures(ItemStack stack) {

        List<ItemLayerTexture> textures = new ArrayList<>();

        Sprite baseSprite = BASE_ID.getSprite();

        textures.add(new ItemLayerTexture(baseSprite, 0xffaaaaaa, 0));

        SpellModifier spellModifier = DataUtils.readObjectFromItemNbt(stack, SpellModifier.class);

        int multi = spellModifier.getMulti();
        int precise = spellModifier.getPrecise();
        int spread = spellModifier.getSpread();

        if(precise >= 1){
            textures.add(new ItemLayerTexture(PRECISE_1_ID.getSprite(), precise > 2 ? GOLD : precise > 1 ? SILVER : BRONZE, 1));
            textures.add(new ItemLayerTexture(PRECISE_2_ID.getSprite(), precise > 2 ? GOLD : BROWN, 1));
            textures.add(new ItemLayerTexture(PRECISE_3_ID.getSprite(), precise > 2 ? GOLD : precise > 1 ? SILVER : BRONZE, 1));
        }
        if(spread >= 1){
            int spreadLayerIndex = precise >= 1 ? 2 : 1;
            textures.add(new ItemLayerTexture(SPREAD_1_ID.getSprite(), spread > 2 ? GOLD : spread > 1 ? SILVER : BRONZE, spreadLayerIndex));
            if(spread >= 3){
                textures.add(new ItemLayerTexture(SPREAD_2_ID.getSprite(), GOLD, spreadLayerIndex));
            }
        }
        if(multi >= 1){
            int multiLayerIndex = 1;
            if(precise >= 1) multiLayerIndex++;
            if(spread >= 1) multiLayerIndex++;

            textures.add(new ItemLayerTexture(MULTI_1_ID.getSprite(), multi > 2 ? GOLD : multi > 1 ? SILVER : BRONZE, multiLayerIndex));
            if(multi >= 3){
                textures.add(new ItemLayerTexture(MULTI_2_ID.getSprite(), GOLD, multiLayerIndex));
            }
        }

        return textures;
    }
}
