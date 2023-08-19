package jipthechip.diabolism.blocks;

import jipthechip.diabolism.Utils.DataUtils;
import jipthechip.diabolism.data.Fluid;
import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import jipthechip.diabolism.items.DiabolismItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFluidContainingBlock extends BlockWithEntity {

    protected AbstractFluidContainingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);
        System.out.println("called abstract onUse");
        if(stackInHand.getItem() == DiabolismItems.BREW_FLUID_BUCKET){
            NbtCompound nbt = player.getStackInHand(hand).getNbt();
            if(nbt != null){
                //System.out.println("nbt not null in abstract onUse");
                String stackFluidStr = nbt.getString("fluid");
                Fluid fluid = (Fluid) DataUtils.DeserializeFromString(stackFluidStr);

                if(fluid != null){
                    //System.out.println("fluid not null in abstract onUse");
                    BlockEntity be = world.getBlockEntity(pos);
                    if(be instanceof AbstractFluidContainer container){
                        int fluidAdded = container.addFluid(fluid);
                        if(fluidAdded > 0){
                            container.syncWithServer();
                            container.markDirty();

                            if(fluidAdded < fluid.getAmount()){
                                fluid.remove(fluidAdded);
                                nbt.putString("fluid", DataUtils.SerializeToString(fluid));
                            }else{
                                System.out.println("set item stack to empty bucket in abstract onUse");
                                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                            }

                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        for (Direction dir : Direction.values()){
            Vec3i vec = dir.getVector();
            BlockPos checkedPos = pos.add(vec);
            BlockState checkedState = world.getBlockState(checkedPos);
            if(checkedState.getBlock() instanceof FluidPipeBlock){
                switch (dir){
                    case NORTH -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.SOUTH, true));
                    }
                    case SOUTH -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.NORTH, true));
                    }
                    case EAST -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.WEST, true));
                    }
                    case WEST -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.EAST, true));
                    }
                    case UP -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.DOWN, true));
                    }
                    case DOWN -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.UP, true));
                    }
                }
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        for (Direction dir : Direction.values()){
            Vec3i vec = dir.getVector();
            BlockPos checkedPos = pos.add(vec);
            BlockState checkedState = world.getBlockState(checkedPos);
            if(checkedState.getBlock() instanceof FluidPipeBlock){
                switch (dir){
                    case NORTH -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.SOUTH, false));
                    }
                    case SOUTH -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.NORTH, false));
                    }
                    case EAST -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.WEST, false));
                    }
                    case WEST -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.EAST, false));
                    }
                    case UP -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.DOWN, false));
                    }
                    case DOWN -> {
                        world.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.UP, false));
                    }
                }
            }
        }
    }

//    @Override
//    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
//        World realWorld = (World) world;
//        for (Direction dir : Direction.values()){
//            Vec3i vec = dir.getVector();
//            BlockPos checkedPos = pos.add(vec);
//            BlockState checkedState = world.getBlockState(checkedPos);
//            if(checkedState.getBlock() instanceof FluidPipeBlock){
//                realWorld.setBlockState(checkedPos, checkedState.with(AbstractOmniDirectionalBlock.SOUTH, false)
//                        .with(AbstractOmniDirectionalBlock.NORTH, false)
//                        .with(AbstractOmniDirectionalBlock.WEST, false)
//                        .with(AbstractOmniDirectionalBlock.EAST, false)
//                        .with(AbstractOmniDirectionalBlock.DOWN, false)
//                        .with(AbstractOmniDirectionalBlock.UP, false)
//                );
//            }
//        }
//    }
}
