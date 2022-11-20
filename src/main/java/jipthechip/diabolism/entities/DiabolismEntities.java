package jipthechip.diabolism.entities;

import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.AltarBlockEntity;
import jipthechip.diabolism.entities.blockentities.AltarBlockEntityRenderer;
import jipthechip.diabolism.entities.blockentities.PillarBlockEntity;
import jipthechip.diabolism.entities.blockentities.RunedBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class DiabolismEntities {

    public static BlockEntityType<AltarBlockEntity> ALTAR_BLOCKENTITY;
    public static BlockEntityType<RunedBlockEntity> RUNED_BLOCKENTITY;
    public static BlockEntityType<PillarBlockEntity> PILLAR_BLOCKENTITY;

    public static EntityType<ProjectileSpellEntity> PROJECTILE_SPELL;

    public static EntityType<ShieldSpellEntity> SHIELD_SPELL;

    public static void registerBlockEntities(){
        ALTAR_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "diabolism:altar_blockentity", FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, DiabolismBlocks.CONDUCTIVE_ALTAR, DiabolismBlocks.MOSSY_ALTAR, DiabolismBlocks.CARVED_ALTAR).build(null));
        RUNED_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "diabolism:runed_blockentity", FabricBlockEntityTypeBuilder.create(RunedBlockEntity::new, DiabolismBlocks.RUNED_COPPER, DiabolismBlocks.RUNED_MOSS, DiabolismBlocks.RUNED_BONE, DiabolismBlocks.RUNED_GLASS).build(null));
        PILLAR_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "diabolism:pillar_blockentity", FabricBlockEntityTypeBuilder.create(PillarBlockEntity::new, DiabolismBlocks.CONDUCTIVE_PILLAR, DiabolismBlocks.MOSSY_PILLAR, DiabolismBlocks.CARVED_PILLAR).build(null));

        PROJECTILE_SPELL = Registry.register(Registry.ENTITY_TYPE, "diabolism:projectile_spell_entity",
                FabricEntityTypeBuilder.<ProjectileSpellEntity>create(SpawnGroup.MISC, ProjectileSpellEntity::new)
                        .dimensions(EntityDimensions.changing(0.5f,0.5f))
                        .trackRangeChunks(64)
                        .build());

        SHIELD_SPELL = Registry.register(Registry.ENTITY_TYPE, "diabolism:shield_spell_entity",
                FabricEntityTypeBuilder.<ShieldSpellEntity>create(SpawnGroup.MISC, ShieldSpellEntity::new)
                        .dimensions(EntityDimensions.changing(0.5f,0.5f))
                        .trackRangeChunks(64)
                        .build());
    }
    public static void registerBlockEntityRenderers(){
        BlockEntityRendererRegistry.register(ALTAR_BLOCKENTITY, AltarBlockEntityRenderer::new);
        EntityRendererRegistry.register(PROJECTILE_SPELL, ProjectileSpellEntityRenderer::new);
        EntityRendererRegistry.register(SHIELD_SPELL, ShieldSpellEntityRenderer::new);
    }
}
