package jipthechip.diabolism.entities;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.*;
import jipthechip.diabolism.packets.BlockEntitySyncPacket;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
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

    public static BlockEntityType<ArcaneAltar> ARCANE_ALTAR_BLOCKENTITY;


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

        MAGIC_CHURNER_BLOCKENTITY = registerBlockEntity(MagicChurner.class, MagicChurner::new, DiabolismBlocks.MAGIC_CHURNER);
        FLUID_PIPE_BLOCKENTITY = registerBlockEntity(FluidPipe.class, FluidPipe::new, DiabolismBlocks.FLUID_PIPE);
        FLUID_PUMP_BLOCKENTITY = registerBlockEntity(FluidPump.class, FluidPump::new, DiabolismBlocks.FLUID_PUMP);
        MAGIC_FERMENTER_BLOCKENTITY = registerBlockEntity(MagicFermenter.class, MagicFermenter::new, DiabolismBlocks.MAGIC_FERMENTER);
        ARCANE_ALTAR_BLOCKENTITY = registerBlockEntity(ArcaneAltar.class, ArcaneAltar::new, DiabolismBlocks.ARCANE_ALTAR);

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

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Class<T> beClass, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block){
        if(AbstractSyncedBlockEntity.class.isAssignableFrom(beClass)){
            addSyncPacket((Class<? extends AbstractSyncedBlockEntity>)beClass);
        }
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, "diabolism:"+block.getClass().getSimpleName().toLowerCase()+"_blockentity", FabricBlockEntityTypeBuilder.create(factory, block).build(null));
    }

    private static <B extends AbstractSyncedBlockEntity> void addSyncPacket(Class<B> beClass){
        System.out.println("Registering Sync Packet for block entity: '"+beClass.getSimpleName()+"'");
        AbstractSyncedBlockEntity.SYNC_PACKETS.put(beClass.getSimpleName().toLowerCase(), BlockEntitySyncPacket.registerSyncPacket(beClass));
    }
}
