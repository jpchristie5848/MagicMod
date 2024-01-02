package jipthechip.diabolism.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.function.ToIntFunction;

public class DiabolismBlocks {
    public static final Block POWDER_COVERED_POLISHED_BLACKSTONE = new PowderCoveredPolishedBlackstoneBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));

    public static final Block DOUBLE_POLISHED_BLACKSTONE = new DoublePolishedBlackstoneBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));

    public static final Block STARTER_CRYSTAL = new StarterCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(createLightLevelFromBlockState(AbstractActivatedBlock.ACTIVATED, 15)));

    public static final Block CRYSTAL_ALTAR = new CrystalAltarBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(createLightLevelFromBlockState(AbstractActivatedBlock.ACTIVATED, 15)));
    public static final Block RUNED_DOUBLE_POLISHED_BLACKSTONE = new RunedDoublePolishedBlackstoneBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE).luminance(createLightLevelFromBlockState(AbstractActivatedBlock.ACTIVATED, 15)));

    public static final Block STARLIGHT_COLLECTOR = new StarlightCollectorBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK));
    public static final Block SUNLIGHT_COLLECTOR = new SunlightCollectorBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK));

    public static final Block MAGIC_CHURNER = new MagicChurnerBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque());

    public static final Block FLUID_PIPE = new FluidPipeBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_FENCE).nonOpaque());

    public static final Block FLUID_PUMP = new FluidPumpBlock(FabricBlockSettings.copyOf(DiabolismBlocks.FLUID_PIPE));

    public static final Block MAGIC_FERMENTER = new MagicFermenterBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque());

    public static final Block ARCANE_ALTAR = new ArcaneAltarBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque());

    public static void initializeClient(){

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.STARTER_CRYSTAL, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CRYSTAL_ALTAR, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.STARLIGHT_COLLECTOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.SUNLIGHT_COLLECTOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.MAGIC_CHURNER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.FLUID_PIPE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.FLUID_PUMP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.MAGIC_FERMENTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.ARCANE_ALTAR, RenderLayer.getCutout());
    }

    public static void registerBlocks(){

        Registry.register(Registries.BLOCK, new Identifier("diabolism", "powder_covered_polished_blackstone"), POWDER_COVERED_POLISHED_BLACKSTONE);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "double_polished_blackstone"), DOUBLE_POLISHED_BLACKSTONE);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "runed_double_polished_blackstone"), RUNED_DOUBLE_POLISHED_BLACKSTONE);

        Registry.register(Registries.BLOCK, new Identifier("diabolism", "starter_crystal"), STARTER_CRYSTAL);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "crystal_altar"), CRYSTAL_ALTAR);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "starlight_collector"), STARLIGHT_COLLECTOR);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "sunlight_collector"), SUNLIGHT_COLLECTOR);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "magic_churner"), MAGIC_CHURNER);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "fluid_pipe"), FLUID_PIPE);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "fluid_pump"), FLUID_PUMP);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "magic_fermenter"), MAGIC_FERMENTER);
        Registry.register(Registries.BLOCK, new Identifier("diabolism", "arcane_altar"), ARCANE_ALTAR);
    }

    private static <J extends Property<T>, T extends Comparable<T>> ToIntFunction<BlockState> createLightLevelFromBlockState(J property, int litLevel) {
        return (state) -> {
            return (Boolean)state.get(property) ? litLevel : 0;
        };
    }
}
