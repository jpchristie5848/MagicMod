package jipthechip.diabolism;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.screen.DiabolismScreens;
import jipthechip.diabolism.events.DiabolismEvents;
import jipthechip.diabolism.items.DiabolismItems;
import jipthechip.diabolism.effect.DiabolismEffects;
import jipthechip.diabolism.packets.DiabolismPackets;
import jipthechip.diabolism.particle.DiabolismParticles;
import jipthechip.diabolism.recipe.DiabolismRecipes;
import jipthechip.diabolism.sound.DiabolismSounds;

import net.fabricmc.api.ModInitializer;

public class Diabolism implements ModInitializer {

    public static final String MOD_ID = "diabolism";

    @Override
    public void onInitialize() {
        DiabolismBlocks.registerBlocks();
        ItemGroups.registerItemGroups();
        DiabolismItems.registerItems();
        DiabolismPackets.registerPacketReceivers();
        DiabolismEvents.registerEvents();
        DiabolismEntities.registerEntities();
        DiabolismRecipes.registerRecipes();
        DiabolismEffects.registerStatusEffects();
        DiabolismEffects.registerPotions();
        DiabolismEffects.registerPotionRecipes();
        DiabolismParticles.registerParticles();
        DiabolismSounds.registerSounds();
        DiabolismScreens.registerExtendedScreenHandlers();
    }
}
