package jipthechip.diabolism.blocks;

import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.blockentities.CrystalAltarBlockEntity;
import jipthechip.diabolism.render.RenderDataMappings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CrystalAltarBlock extends AbstractCrystalAltarBlock{
    public CrystalAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Stream.of(
                Block.createCuboidShape(0, 0, 0, 16, 1, 16),
                Block.createCuboidShape(13, 1, 3, 14, 3, 13),
                Block.createCuboidShape(2, 1, 3, 3, 3, 13),
                Block.createCuboidShape(3, 1, 2, 13, 3, 14),
                Block.createCuboidShape(4, 3, 4, 12, 4, 12),
                Block.createCuboidShape(6, 4, 6, 10, 7, 10),
                Block.createCuboidShape(4, 7, 4, 12, 8, 12),
                Block.createCuboidShape(12, 8, 3, 13, 9, 12),
                Block.createCuboidShape(13, 9, 2, 14, 10, 13),
                Block.createCuboidShape(14, 10, 2, 15, 12, 14),
                Block.createCuboidShape(2, 10, 1, 14, 12, 2),
                Block.createCuboidShape(2, 10, 14, 14, 12, 15),
                Block.createCuboidShape(1, 10, 2, 2, 12, 14),
                Block.createCuboidShape(2, 9, 3, 3, 10, 14),
                Block.createCuboidShape(2, 9, 2, 13, 10, 3),
                Block.createCuboidShape(3, 9, 13, 14, 10, 14),
                Block.createCuboidShape(3, 8, 4, 4, 9, 13),
                Block.createCuboidShape(4, 8, 12, 13, 9, 13),
                Block.createCuboidShape(3, 8, 3, 12, 9, 4)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalAltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, DiabolismEntities.CRYSTAL_ALTAR_BLOCKENTITY, CrystalAltarBlockEntity::ticker);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        CrystalAltarBlockEntity crystalAltarBlockEntity = (CrystalAltarBlockEntity) world.getBlockEntity(pos);
        if (crystalAltarBlockEntity != null){
            ItemStack stack = player.getStackInHand(hand);
            if(stack.getItem() == Items.IRON_SWORD){
                System.out.println("adding fluid to altar");
                crystalAltarBlockEntity.addFluid(100, RenderDataMappings.Fluids.get("starlight"));
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, (float) (Math.random() + 1.0f), (float) (Math.random() + 1.0f), true);
            }
            else if(stack.getItem() == Items.BUCKET){
                crystalAltarBlockEntity.emptyFluid();
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, (float) (Math.random() + 1.0f), (float) (Math.random() + 1.0f), true);
            }
            else if(crystalAltarBlockEntity.setStoredItem(stack.getItem()) && !stack.isEmpty()){
                System.out.println("doing normal item add");
                int count = stack.getCount();
                if(count > 1){
                    count--;
                    stack.setCount(count);
                }else{
                    player.setStackInHand(hand, ItemStack.EMPTY);
                }
            }

        }
        return ActionResult.SUCCESS;
    }
}
