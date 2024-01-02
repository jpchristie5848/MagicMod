package jipthechip.diabolism.entities;

import jipthechip.diabolism.Utils.BlockHelpers;
import jipthechip.diabolism.Utils.TargetCondition;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import jipthechip.diabolism.render.RenderDataMappings;
import jipthechip.diabolism.render.FluidRenderData;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagickaParticleEntity extends ParticleSpawningEntity{


    int tickCounter = 0;
    List<BlockPos> path;

    Block sourceBlock;

    BlockPos source;
    BlockPos dest;

    int potency;

    String magickaType;

    // condition that must be met in order for a target to be selected
    private final TargetCondition CAN_ADD_FLUID = (blockEntity) -> {
        AbstractFluidContainer container = (AbstractFluidContainer) blockEntity;
        return (container.getFluidRenderData() == null || container.getFluidRenderData() == RenderDataMappings.Fluids.get(magickaType)) && !container.isFull();
    };

    public MagickaParticleEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public MagickaParticleEntity(EntityType<?> type, World world, String magickaType, int potency, Vec3d startPos, Vec3d startVelocity){
        super(type, world);
        this.magickaType = magickaType;
        this.potency = potency;
        this.setPosition(startPos);
        this.setVelocity(startVelocity);
        //BlockPos pos = this.getBlockPos();
        //path = BlockHelpers.BlockBFS(world, DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE, DiabolismBlocks.CRYSTAL_ALTAR, pos, CAN_ADD_FLUID);
        //this.setVelocity(new Vec3d(path.get(1).getX()-pos.getX(), 0, path.get(1).getZ()-pos.getZ()).normalize().multiply(0.1));
    }

    @Override
    public void tick() {
        super.tick();
        tickCounter++;

        if(tickCounter % 5 == 0){
            //System.out.println("particle velocity: "+getVelocity().toString());
            if(this.getVelocity().equals(new Vec3d(0,0,0))){
                this.kill();
            }
            BlockPos blockPos = this.getBlockPos();

            path = BlockHelpers.BlockBFS(getWorld(), DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE, DiabolismBlocks.CRYSTAL_ALTAR, blockPos, CAN_ADD_FLUID);

            BlockPos containerPos = null;
            AbstractFluidContainer container = null;

            if(!path.isEmpty()){
                containerPos = path.get(path.size()-1);
                container = (AbstractFluidContainer) getWorld().getBlockEntity(containerPos);
            }

            FluidRenderData fluidToAdd = RenderDataMappings.Fluids.get(magickaType);


            if(path.isEmpty() || container == null || !(container.getFluidRenderData() == null || container.getFluidRenderData() == fluidToAdd) || container.isFull()){
                this.kill();
            }else if (blockPos.equals(path.get(path.size()-2)) && passedBlockCenter(0.2f)){
                container.addFluid(potency * 2, RenderDataMappings.Fluids.get(magickaType));
                getWorld().updateListeners(containerPos, getWorld().getBlockState(containerPos), getWorld().getBlockState(containerPos), Block.NOTIFY_LISTENERS);
                this.kill();
            }else if (passedBlockCenter()){
                Vec3d newVelocity = new Vec3d(path.get(1).getX()-blockPos.getX(), 0, path.get(1).getZ()-blockPos.getZ()).normalize().multiply(0.05);
                if(!this.getVelocity().equals(newVelocity)){
                    setPosition(blockPos.getX()+0.5, blockPos.getY() + 0.5, blockPos.getZ()+0.5);
                    this.setVelocity(newVelocity);
                }
            }
        }
        if(tickCounter % 2 == 0){
            this.setPosition(this.getPos().add(this.getVelocity()));
            playParticles(null);
        }
    }

    // checks if the entity has moved the specified distance past the block center
    public boolean passedBlockCenter(float distance){
        Vec3d velocity = this.getVelocity();
        Vec3d pos = this.getPos();

        if(velocity.getX() != 0){
            return velocity.getX() > 0 ? pos.getX() - Math.floor(pos.getX()) > (0.5 + distance) : pos.getX() - Math.floor(pos.getX()) < (0.5 - distance);
        }else if(velocity.getZ() != 0){
            return velocity.getZ() > 0 ? pos.getZ() - Math.floor(pos.getZ()) > (0.5 + distance) : pos.getZ() - Math.floor(pos.getZ()) < (0.5 - distance);
        }
        return false;
    }

    public boolean passedBlockCenter(){
        return passedBlockCenter(0.0f);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    protected void playParticles(@Nullable PlayerEntity playerEntity) {
        //System.out.println("particle pos: "+this.getPos().toString());
        if(!getWorld().isClient() && RenderDataMappings.Fluids.get(magickaType) != null){
            int numParticles = potency / 5 + 1;
            for(int i = 0; i < numParticles; i++){
                double xOffset = (Math.random() * 0.1)-0.05;
                double yOffset = (Math.random() * 0.1)-0.05;
                double zOffset = (Math.random() * 0.1)-0.05;

                PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) getWorld()).spawnParticles(player,
                        ColoredSpellParticleFactory.createData(RenderDataMappings.Fluids.get(magickaType).getColor(), (int)(Math.random()*8+2)), true, getPos().getX()+xOffset, getPos().getY()+yOffset, getPos().getZ()+zOffset, 1,
                        0, 0, 0, 0));
            }
        }
    }

}
