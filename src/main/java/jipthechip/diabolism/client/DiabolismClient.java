package jipthechip.diabolism.client;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.client.models.DiabolismModelResourceProvider;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.geo.ArcaneAltarBlockRenderer;
import jipthechip.diabolism.entities.blockentities.geo.MagicChurnerBlockRenderer;
import jipthechip.diabolism.entities.blockentities.geo.FluidPumpBlockRenderer;
import jipthechip.diabolism.entities.blockentities.geo.MagicFermenterBlockRenderer;
import jipthechip.diabolism.screen.DiabolismScreens;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.particle.DiabolismParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class DiabolismClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DiabolismBlocks.initializeClient();
        DiabolismItems.initializeClient();
        DiabolismEntities.registerEntityRenderers();
        DiabolismParticles.registerParticlesClient();
        DiabolismScreens.registerScreenHandlers();
        DiabolismScreens.registerHandledScreens();


        //ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm->new AltarFluidModelProvider());
        //GeoItemRenderer.registerItemRenderer(DiabolismItems.MAGIC_CHURNER_BLOCKITEM, new MagicChurnerItemRenderer());
        BlockEntityRendererFactories.register(DiabolismEntities.MAGIC_CHURNER_BLOCKENTITY, MagicChurnerBlockRenderer::new);
        BlockEntityRendererFactories.register(DiabolismEntities.FLUID_PUMP_BLOCKENTITY, FluidPumpBlockRenderer::new);
        BlockEntityRendererFactories.register(DiabolismEntities.MAGIC_FERMENTER_BLOCKENTITY, MagicFermenterBlockRenderer::new);
        BlockEntityRendererFactories.register(DiabolismEntities.ARCANE_ALTAR_BLOCKENTITY, ArcaneAltarBlockRenderer::new);
        //BlockEntityRendererRegistry.register(DiabolismEntities.MAGIC_CHURNER_BLOCKENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new MagicChurnerBlockRenderer());

        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm-> new DiabolismModelResourceProvider());
    }


}
