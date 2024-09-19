package jipthechip.diabolism.entities.blockentities.geo;

import jipthechip.diabolism.Diabolism;
import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.spell.SpellModifier;
import jipthechip.diabolism.entities.blockentities.AbstractMagickaConsumerBlockRenderer;
import jipthechip.diabolism.entities.blockentities.ArcaneAltar;
import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.FastBoneFilterGeoLayer;

import java.util.List;

public class ArcaneAltarBlockRenderer extends AbstractMagickaConsumerBlockRenderer<ArcaneAltar> {

    public ArcaneAltarBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(new ArcaneAltarBlockModel());

        addRenderLayer(new ArcaneAltarBlockRenderLayer(this));

        addRenderLayer(new FastBoneFilterGeoLayer<>(this, ()-> List.of("focus0", "focus1"), ((geoBone, altar, aFloat) -> {
            ItemStack focusStack = altar.getStack(2);
            geoBone.setHidden(focusStack.isEmpty() || focusStack.getItem() != DiabolismItems.SPELL_MODIFIER);
        })));

        addRenderLayer(new FastBoneFilterGeoLayer<>(this, ()-> List.of("orb0"), ((geoBone, altar, aFloat) -> {
            ItemStack orbStack = altar.getStack(1);
            geoBone.setHidden(orbStack.isEmpty() || orbStack.getItem() != DiabolismItems.SPELL_TEMPLATE);
        })));
    }

    @Nullable
    @Override
    protected Identifier getTextureOverrideForBone(GeoBone bone, ArcaneAltar altar, float partialTick) {
        String boneName = bone.getName();

        if(boneName.equals("focus0")){
            ItemStack stack = altar.getStack(2);
            if(!stack.isEmpty() && stack.getItem() == DiabolismItems.SPELL_MODIFIER){
                SpellModifier focus = DataUtils.readObjectFromItemNbt(stack, SpellModifier.class);
                if(focus != null){
                    int textureIndex = focus.spread() + 3;
                    Identifier texture = new Identifier(Diabolism.MOD_ID, "textures/block/arcane_altar_spread_"+textureIndex+".png");
                    return texture;
                }
            }
        }

        else if(boneName.equals("focus1")){
            ItemStack stack = altar.getStack(2);
            SpellModifier focus = DataUtils.readObjectFromItemNbt(stack, SpellModifier.class);
            if(focus != null){
                int textureIndex = focus.multi();
                return new Identifier(Diabolism.MOD_ID, "textures/block/arcane_altar_multi_"+textureIndex+".png");
            }
        }
//        else if(boneName.equals("orb0")){
//
//        }
        return null;
    }
}
