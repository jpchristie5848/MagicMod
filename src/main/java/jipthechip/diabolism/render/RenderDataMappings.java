package jipthechip.diabolism.render;

import java.util.HashMap;
import java.util.Map;

public class RenderDataMappings {

    public static final Map<String, FluidRenderData> Fluids = new HashMap<>(){{
        put("blood", new FluidRenderData("blood", -8051676, 0, 10,
                "block/water_still", "block/water_flow"));
        put("starlight", new FluidRenderData("starlight", 0xFF8DA0FF, 255, 10,
                "block/water_still", "block/water_flow"));
        put("sunlight", new FluidRenderData("sunlight", 0xFFF3D7A0, 255, 10,
                "block/water_still", "block/water_flow"));
        put("churner_fluid", new FluidRenderData("churner_fluid", 0xFF1C4D9C, 0, 10,
                "block/water_still", "block/water_flow"));
    }};

    public static final Map<String, CuboidRenderData> Solids = new HashMap<>(){{
       put("ice", new CuboidRenderData("ice", 0x1Fa1e5f7, 100, 0, "block/ice"));
    }};
}
