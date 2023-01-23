package jipthechip.diabolism.events;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.event.WindowResizeCallback;
import jipthechip.diabolism.Utils.IMagicProperties;
import jipthechip.diabolism.mixin.HudMixin;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class UIEvents {


    private static OwoUIAdapter<FlowLayout> adapter;
    private static final Identifier MAGICKA_ICON_FULL = new Identifier("diabolism:textures/ui/magicka_icon_full.png");
    private static final Identifier MAGICKA_ICON_HALF = new Identifier("diabolism:textures/ui/magicka_icon_half.png");
    private static final Identifier MAGICKA_ICON_EMPTY = new Identifier("diabolism:textures/ui/magicka_icon_empty.png");

    private static final List<Identifier> MagickaBarIcons = new ArrayList<>();
    private static final List<Component> MagickaBarComponents = new ArrayList<>();

    private static final float MAGICKA_BAR_RELATIVE_POS_X = 0.05f;
    private static final float MAGICKA_BAR_RELATIVE_POS_Y = 0.9f;
    private static final int MAGICKA_ICON_SPACING = 12;

    public static void registerEvents(){

        ClientLifecycleEvents.CLIENT_STARTED.register((client -> {
            if(client.player != null && adapter != null) {
                for(int i = MagickaBarComponents.size()-1; i >= 0; i--) {
                    removeMagickaBarIcon(i);
                }
            }
        }));

        WindowResizeCallback.EVENT.register((client, window) -> {
            for(int i = 0; i < MagickaBarComponents.size(); i++){
                updateMagickaBarIcon(i, window);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if(HudMixin.getAdapter() == null) {
                HudMixin.intializeAdapter();
            }
            if(HudMixin.getAdapter() != null && adapter == null){
                adapter = HudMixin.getAdapter();
            }
            updateMagickaBar(client, client.getWindow());
        }));
    }

    private static void updateMagickaBar(MinecraftClient client, Window window){
        if(client.player != null && adapter != null){

            FlowLayout rootComponent = adapter.rootComponent;

            int oldNumComponents = MagickaBarComponents.size();

            int magicka = ((IMagicProperties)client.player).getMagicka();
            int maxMagicka = ((IMagicProperties)client.player).getMaxMagicka();

            int newNumComponents = maxMagicka / 2;

            // remove magic bar icons if max magicka has decreased
            if(oldNumComponents > newNumComponents){
                for(int i = oldNumComponents-1; i >= oldNumComponents - (oldNumComponents - newNumComponents); i--){
                    removeMagickaBarIcon(i);
                }
            }

            // number of full icons
            int numFull = magicka / 2;
            // number of half icons
            int numHalf = magicka % 2;

            for(int i = 0; i < newNumComponents; i++){

                // check which icon the current component needs to be (empty, half, or full)
                Identifier iconType = i >= MagickaBarIcons.size() ? null : MagickaBarIcons.get(i);
                Identifier newIconType;
                if(numFull > 0){
                    newIconType = MAGICKA_ICON_FULL;
                    numFull--;
                }else if(numHalf > 0){
                    newIconType = MAGICKA_ICON_HALF;
                    numHalf--;
                }else {
                    newIconType = MAGICKA_ICON_EMPTY;
                }

                // check if new icon needs to be added, or update existing one
                if(i >= oldNumComponents){
                    addMagickaBarIcon(i, newIconType, window);
                }else if (rootComponent.children().size() >=  i+1){
                    if(!newIconType.equals(iconType)){
                        updateMagickaBarIcon(i, newIconType, window);
                    }
                }
            }
        }
    }

    private static void addMagickaBarIcon(int index, Identifier newIconType, Window window){
        if(adapter != null){
            int windowWidth = window.getScaledWidth();
            int windowHeight = window.getScaledHeight();

            int xPos = (int) (windowWidth*(MAGICKA_BAR_RELATIVE_POS_X)+((index -1)*MAGICKA_ICON_SPACING));
            int yPos = (int) (windowHeight * (MAGICKA_BAR_RELATIVE_POS_Y));

            FlowLayout rootComponent = adapter.rootComponent;
            MagickaBarIcons.add(newIconType);
            MagickaBarComponents.add(Components.texture(newIconType, 0, 0, 16, 16, 16, 16).positioning(Positioning.absolute(xPos, yPos)));
            rootComponent.child(MagickaBarComponents.get(index));
        }
    }
    private static void removeMagickaBarIcon(int index){
        if(adapter != null){
            FlowLayout rootComponent = adapter.rootComponent;
            rootComponent.removeChild(MagickaBarComponents.get(index));
            MagickaBarComponents.remove(index);
            MagickaBarIcons.remove(index);
        }
    }

    private static void updateMagickaBarIcon(int index, Window window){
        if(adapter != null){
            int windowWidth = window.getScaledWidth();
            int windowHeight = window.getScaledHeight();

            int xPos = (int) (windowWidth*(MAGICKA_BAR_RELATIVE_POS_X)+((index -1)*MAGICKA_ICON_SPACING));
            int yPos = (int) (windowHeight * (MAGICKA_BAR_RELATIVE_POS_Y));

            MagickaBarComponents.get(index).positioning(Positioning.absolute(xPos, yPos));
        }

    }

    private static void updateMagickaBarIcon(int index, Identifier newIconType, Window window){
        if(adapter != null){

            int windowWidth = window.getScaledWidth();
            int windowHeight = window.getScaledHeight();

            int xPos = (int) (windowWidth*(MAGICKA_BAR_RELATIVE_POS_X)+((index -1)*MAGICKA_ICON_SPACING));
            int yPos = (int) (windowHeight * (MAGICKA_BAR_RELATIVE_POS_Y));

            FlowLayout rootComponent = adapter.rootComponent;

            MagickaBarIcons.set(index, newIconType);
            rootComponent.removeChild(MagickaBarComponents.get(index));
            MagickaBarComponents.set(index, Components.texture(newIconType, 0, 0, 16, 16, 16, 16).positioning(Positioning.absolute(xPos, yPos)));
            rootComponent.child(MagickaBarComponents.get(index));
        }
    }
}
