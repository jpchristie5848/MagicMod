package jipthechip.diabolism.client.models.items;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.SpellModifier;
import jipthechip.diabolism.data.Wizwich;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class WizwichTextureProvider implements ItemLayerTextureProvider{

    private static final SpriteIdentifier BREAD = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/bread")); //"diabolism:item/spell_modifier_base"

    private static final SpriteIdentifier LETTUCE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/lettuce"));
    private static final SpriteIdentifier GUACAMOLE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/guacamole"));
    private static final SpriteIdentifier BACON = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/bacon"));
    private static final SpriteIdentifier TOMATO = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/tomato"));
    private static final SpriteIdentifier QUESO = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("diabolism:item/queso"));

    private final int color = -1;

    @Override
    public List<ItemLayerTexture> getTextures(ItemStack stack) {

        List<ItemLayerTexture> textures = new ArrayList<>();

        textures.add(new ItemLayerTexture(BREAD.getSprite(), color, 0));

        int layer = 1;

        Wizwich wizwich = DataUtils.readObjectFromItemNbt(stack, Wizwich.class);

        if(wizwich != null){
            String ingredients = wizwich.getIngredients();
            for(char c : ingredients.toCharArray()){
                switch (c) {
                    case 'l' -> {
                        textures.add(new ItemLayerTexture(LETTUCE.getSprite(), color, layer));
                        layer++;
                    }
                    case 'g' -> {
                        textures.add(new ItemLayerTexture(GUACAMOLE.getSprite(), color, layer));
                        layer++;
                    }
                    case 'b' -> {
                        textures.add(new ItemLayerTexture(BACON.getSprite(), color, layer));
                        layer++;
                    }
                    case 't' -> {
                        textures.add(new ItemLayerTexture(TOMATO.getSprite(), color, layer));
                        layer++;
                    }
                    case 'q' -> {
                        textures.add(new ItemLayerTexture(QUESO.getSprite(), color, layer));
                        layer++;
                    }
                }
            }
        }

        textures.add(new ItemLayerTexture(BREAD.getSprite(), color, layer));

        return textures;
    }
}
