package jipthechip.diabolism.entities.blockentities.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class DiabolismScreens {

    public static ScreenHandlerType<ArcaneAltarScreenHandler> ARCANE_ALTAR_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(ArcaneAltarScreenHandler::new);



    public static void registerScreenHandlers(){

    }

    public static void registerHandledScreens(){
        HandledScreens.register(ARCANE_ALTAR_SCREEN_HANDLER, ArcaneAltarScreen::new);
    }

    public static void registerExtendedScreenHandlers(){
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("diabolism", "arcane_altar"), ARCANE_ALTAR_SCREEN_HANDLER);
    }
}
