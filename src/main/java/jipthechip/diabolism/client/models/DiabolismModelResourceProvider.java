package jipthechip.diabolism.client.models;

import jipthechip.diabolism.client.models.items.MagickaCrystalModel;
import jipthechip.diabolism.client.models.items.SpellModifierModel;
import jipthechip.diabolism.client.models.items.SpellTemplateModel;
import jipthechip.diabolism.client.models.items.WizwichModel;
import jipthechip.diabolism.data.SpellTemplate;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class DiabolismModelResourceProvider implements ModelResourceProvider {

    private static final Identifier SPELL_MODIFIER_ID = new Identifier("diabolism:item/spell_modifier");
    private static final Identifier WIZWICH_ID = new Identifier("diabolism:item/wizwich");

    private static final Identifier MAGICKA_CRYSTAL_ID = new Identifier("diabolism:item/magicka_crystal");
    private static final Identifier SPELL_TEMPLATE_ID = new Identifier("diabolism:item/spell_template");
    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context) throws ModelProviderException {
        if(resourceId.equals(SPELL_MODIFIER_ID)){
            return new SpellModifierModel();
        }else if(resourceId.equals(WIZWICH_ID)){
            return new WizwichModel();
        }else if(resourceId.equals(MAGICKA_CRYSTAL_ID)){
            return new MagickaCrystalModel();
        }
        else if(resourceId.equals(SPELL_TEMPLATE_ID)){
            return new SpellTemplateModel();
        }
        else{
            return null;
        }
    }
}
