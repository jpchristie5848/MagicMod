package jipthechip.diabolism.blocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.data.DataGenerator;

public class DiabolismBlockTagProvider extends FabricTagProvider.BlockTagProvider {


    public DiabolismBlockTagProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(BlockTags.DIRT).add(new Block[]{DiabolismBlocks.MOSSY_PILLAR});
    }
}
