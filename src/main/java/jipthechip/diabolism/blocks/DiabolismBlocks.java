package jipthechip.diabolism.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.ExperienceOrbEntityRenderer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.layer.LayerGlowingAreasGeo;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class DiabolismBlocks {
    public static final Block RUNED_GLASS = new RunedGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS));
    public static final Block RUNED_COPPER = new RunedCopperBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block RUNED_MOSS = new RunedMossBlock(FabricBlockSettings.of(Material.MOSS_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block RUNED_BONE = new RunedBoneBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));

    public static final Block CONDUCTIVE_ALTAR = new ConductiveAltarBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block MOSSY_ALTAR = new MossyAltarBlock(FabricBlockSettings.copyOf(Blocks.MOSSY_COBBLESTONE).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block CARVED_ALTAR = new CarvedAltarBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));

    public static final Block CONDUCTIVE_PILLAR = new ConductivePillarBlock(FabricBlockSettings.of(Material.METAL).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block MOSSY_PILLAR = new MossyPillarBlock(FabricBlockSettings.of(Material.STONE).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));
    public static final Block CARVED_PILLAR = new CarvedPillarBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).luminance(createLightLevelFromBlockState(AbstractAltarComponentBlock.ACTIVATED, 15)));

    public static final Block POWDER_COVERED_POLISHED_BLACKSTONE = new PowderCoveredPolishedBlackstoneBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));

    public static final Block DOUBLE_POLISHED_BLACKSTONE = new DoublePolishedBlackstoneBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE));

    public static void initializeClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.RUNED_GLASS, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CONDUCTIVE_ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.MOSSY_ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CARVED_ALTAR, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CONDUCTIVE_PILLAR, RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CONDUCTIVE_PILLAR, RenderLayer.);

        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.MOSSY_PILLAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(DiabolismBlocks.CARVED_PILLAR, RenderLayer.getCutout());
    }

    public static void registerBlocks(){
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "runed_glass"), RUNED_GLASS);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "runed_copper"), RUNED_COPPER);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "runed_moss"), RUNED_MOSS);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "runed_bone"), RUNED_BONE);

        Registry.register(Registry.BLOCK, new Identifier("diabolism", "conductive_altar"), CONDUCTIVE_ALTAR);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "mossy_altar"), MOSSY_ALTAR);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "carved_altar"), CARVED_ALTAR);

        Registry.register(Registry.BLOCK, new Identifier("diabolism", "conductive_pillar"), CONDUCTIVE_PILLAR);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "mossy_pillar"), MOSSY_PILLAR);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "carved_pillar"), CARVED_PILLAR);

        Registry.register(Registry.BLOCK, new Identifier("diabolism", "powder_covered_polished_blackstone"), POWDER_COVERED_POLISHED_BLACKSTONE);
        Registry.register(Registry.BLOCK, new Identifier("diabolism", "double_polished_blackstone"), DOUBLE_POLISHED_BLACKSTONE);
    }

    private static <J extends Property<T>, T extends Comparable<T>> ToIntFunction<BlockState> createLightLevelFromBlockState(J property, int litLevel) {
        return (state) -> {
            return (Boolean)state.get(property) ? litLevel : 0;
        };
    }
}
