package jipthechip.diabolism.entities;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DiabolismEntities {

    // block entities

    public static BlockEntityType<RunedBlackstoneBlockEntity> RUNED_BLACKSTONE_BLOCKENTITY;

    public static BlockEntityType<StarlightCollectorBlockEntity> STARLIGHT_COLLECTOR_BLOCKENTITY;

    public static BlockEntityType<SunlightCollectorBlockEntity> SUNLIGHT_COLLECTOR_BLOCKENTITY;

    public static BlockEntityType<StarterCrystalBlockEntity> STARTER_CRYSTAL_BLOCKENTITY;

    public static BlockEntityType<CrystalAltarBlockEntity> CRYSTAL_ALTAR_BLOCKENTITY;

    public static BlockEntityType<MagicChurner> MAGIC_CHURNER_BLOCKENTITY;

    public static BlockEntityType<FluidPipe> FLUID_PIPE_BLOCKENTITY;

    public static BlockEntityType<FluidPump> FLUID_PUMP_BLOCKENTITY;

    public static BlockEntityType<MagicFermenter> MAGIC_FERMENTER_BLOCKENTITY;


    // entities

    public static EntityType<ProjectileSpellEntity> PROJECTILE_SPELL;

    public static EntityType<ShieldSpellEntity> SHIELD_SPELL;

    public static EntityType<WatcherEntity> WATCHER;

    public static EntityType<MagickaParticleEntity> MAGICKA_PARTICLE;

    public static void registerEntities(){
        STARTER_CRYSTAL_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:starter_crystal_blockentity", FabricBlockEntityTypeBuilder.create(StarterCrystalBlockEntity::new, DiabolismBlocks.STARTER_CRYSTAL).build(null));
        RUNED_BLACKSTONE_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:runed_blackstone_blockentity", FabricBlockEntityTypeBuilder.create(RunedBlackstoneBlockEntity::new, DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE).build(null));
        CRYSTAL_ALTAR_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:crystal_altar_blockentity", FabricBlockEntityTypeBuilder.create(CrystalAltarBlockEntity::new, DiabolismBlocks.CRYSTAL_ALTAR).build(null));
        STARLIGHT_COLLECTOR_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:starlight_collector_blockentity", FabricBlockEntityTypeBuilder.create(StarlightCollectorBlockEntity::new, DiabolismBlocks.STARLIGHT_COLLECTOR).build(null));
        SUNLIGHT_COLLECTOR_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:sunlight_collector_blockentity", FabricBlockEntityTypeBuilder.create(SunlightCollectorBlockEntity::new, DiabolismBlocks.SUNLIGHT_COLLECTOR).build(null));
        MAGIC_CHURNER_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:magic_churner_blockentity", FabricBlockEntityTypeBuilder.create(MagicChurner::new, DiabolismBlocks.MAGIC_CHURNER).build(null));
        FLUID_PIPE_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:fluid_pipe_blockentity", FabricBlockEntityTypeBuilder.create(FluidPipe::new, DiabolismBlocks.FLUID_PIPE).build(null));
        FLUID_PUMP_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:fluid_pump_blockentity", FabricBlockEntityTypeBuilder.create(FluidPump::new, DiabolismBlocks.FLUID_PUMP).build(null));
        MAGIC_FERMENTER_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:magic_fermenter_blockentity", FabricBlockEntityTypeBuilder.create(MagicFermenter::new, DiabolismBlocks.MAGIC_FERMENTER).build(null));

        PROJECTILE_SPELL = Registry.register(Registries.ENTITY_TYPE, "diabolism:projectile_spell_entity",
                FabricEntityTypeBuilder.<ProjectileSpellEntity>create(SpawnGroup.MISC, ProjectileSpellEntity::new)
                        .dimensions(EntityDimensions.changing(0.1f,0.1f))
                        .trackRangeChunks(64)
                        .build());

        SHIELD_SPELL = Registry.register(Registries.ENTITY_TYPE, "diabolism:shield_spell_entity",
                FabricEntityTypeBuilder.<ShieldSpellEntity>create(SpawnGroup.MISC, ShieldSpellEntity::new)
                        .dimensions(EntityDimensions.changing(0.5f,0.5f))
                        .trackRangeChunks(64)
                        .build());

        WATCHER = Registry.register(Registries.ENTITY_TYPE, "diabolism:watcher_entity",
                FabricEntityTypeBuilder.<WatcherEntity>create(SpawnGroup.MISC, WatcherEntity::new)
                        .dimensions(EntityDimensions.changing(0.5f,0.5f))
                        .trackRangeChunks(64)
                        .build());

        MAGICKA_PARTICLE = Registry.register(Registries.ENTITY_TYPE, "diabolism:magicka_particle",
                FabricEntityTypeBuilder.<MagickaParticleEntity>create(SpawnGroup.MISC, MagickaParticleEntity::new)
                        .dimensions(EntityDimensions.changing(0.1f,0.1f))
                        .trackRangeChunks(64)
                        .build());




    }
    public static void registerEntityRenderers(){
        //BlockEntityRendererRegistry.register(CRYSTAL_ALTAR_BLOCKENTITY, CrystalAltarBlockEntityRenderer::new);
        //BlockEntityRendererRegistry.register(MAGIC_CHURNER_BLOCKENTITY, MagicChurnerRenderer::new);
        BlockEntityRendererFactories.register(CRYSTAL_ALTAR_BLOCKENTITY, CrystalAltarBlockEntityRenderer::new);
        //BlockEntityRendererFactories.register(MAGIC_CHURNER_BLOCKENTITY, MagicChurnerRenderer::new);

        EntityRendererRegistry.register(PROJECTILE_SPELL, ProjectileSpellEntityRenderer::new);
        EntityRendererRegistry.register(SHIELD_SPELL, ShieldSpellEntityRenderer::new);
        EntityRendererRegistry.register(WATCHER, WatcherEntityRenderer::new);
        EntityRendererRegistry.register(MAGICKA_PARTICLE, MagickaParticleEntityRenderer::new);
    }
}
