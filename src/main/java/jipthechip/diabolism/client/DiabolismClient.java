package jipthechip.diabolism.client;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.particle.DiabolismParticles;
import net.fabricmc.api.ClientModInitializer;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class DiabolismClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DiabolismBlocks.initializeClient();
        DiabolismItems.initializeClient();
        DiabolismEntities.registerEntityRenderers();
        DiabolismParticles.registerParticlesClient();
    }


}
