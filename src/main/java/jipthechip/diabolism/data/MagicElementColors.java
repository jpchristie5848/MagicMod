package jipthechip.diabolism.data;

import java.util.HashMap;

public class MagicElementColors {

    public static final HashMap<MagicElement, Integer> MAP = new HashMap<>(){{
        put(MagicElement.AIR, 0xffebe3a2);
        put(MagicElement.FIRE, 0xffed3f1c);
        put(MagicElement.WATER, 0xff1c7eed);
        put(MagicElement.EARTH, 0xff75430d);
        put(MagicElement.CHAOS, 0xff000000);
        put(MagicElement.ORDER, 0xffffffff);
        put(MagicElement.LIGHTNING, 0xffeeff00);
        put(MagicElement.ICE, 0xffa3daf0);
        put(MagicElement.LIFE, 0xff22e305);
        put(MagicElement.DEATH, 0xff000000);
        put(MagicElement.SPIRIT, 0xfff0dcf5);
    }};
}
