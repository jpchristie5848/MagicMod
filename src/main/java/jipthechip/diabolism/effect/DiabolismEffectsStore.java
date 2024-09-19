package jipthechip.diabolism.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.*;

public class DiabolismEffectsStore {

    private final Map<String, Map<String, DiabolismStatusEffect>> STATUS_EFFECTS;

    public DiabolismEffectsStore(){
        STATUS_EFFECTS = new HashMap<>();
    }

    public void register(DiabolismStatusEffect ... effects){

        for(DiabolismStatusEffect effect : effects){

            String category = getEffectCategories(effect).get(0);
            if(!STATUS_EFFECTS.containsKey(category)){
                STATUS_EFFECTS.put(category, new HashMap<>());
            }
            STATUS_EFFECTS.get(category).put(effect.registryName, effect);
            System.out.println("Registering status effect in category '"+category+"': "+new Identifier("diabolism", effect.getRegistryName()));

            Registry.register(Registries.STATUS_EFFECT, new Identifier("diabolism", effect.getRegistryName()), effect);
        }
    }

    public Map<String, DiabolismStatusEffect> getByCategory(String category){
        return STATUS_EFFECTS.get(category);
    }

    public DiabolismStatusEffect get(String category, String name){
        return getByCategory(category).get(name);
    }

    public DiabolismStatusEffect get(String name){
        for(String category : STATUS_EFFECTS.keySet()){
            Map<String, DiabolismStatusEffect> effectsMap = STATUS_EFFECTS.get(category);
            for(String registryName : effectsMap.keySet()){
                if(registryName.equals(name)){
                    return effectsMap.get(registryName);
                }
            }
        }
        return null;
    }

    public static List<String> getEffectCategories(DiabolismStatusEffect effect){
        List<String> categories = new ArrayList<>();
        Class c = effect.getClass();
        while(c != DiabolismStatusEffect.class){
            categories.add(c.getSimpleName().replace("Abstract", "")
                    .replace("StatusEffect", "").toLowerCase());
            c = c.getSuperclass();
        }
        return categories;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("DiabolismEffectStore {");
        for(String category : STATUS_EFFECTS.keySet()){
            builder.append("\n\t").append(category).append(":");
            Map<String, DiabolismStatusEffect> effectMap = STATUS_EFFECTS.get(category);
            for(String registryName : effectMap.keySet()){
                builder.append("\n\t\t").append(registryName).append(":").append(effectMap.get(registryName));
            }
        }
        builder.append("\n}");
        return builder.toString();
    }
}
