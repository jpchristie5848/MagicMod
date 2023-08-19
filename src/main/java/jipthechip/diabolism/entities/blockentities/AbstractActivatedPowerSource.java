package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.BlockHelpers;
import jipthechip.diabolism.blocks.AbstractActivatedBlock;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.particle.ColoredSpellParticleFactory;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivatedPowerSource extends BlockEntity {

    int tickCounter = 0;
    List<BlockPos> path;

    List<BlockPos> lastPath;

    List<Vec3d> particlePositions = new ArrayList<>();

    // condition that must be met in order for it to select a target
    //TargetCondition targetCondition = (blockEntity) -> !world.getBlockState(blockEntity.getPos()).get(AbstractActivatedBlock.ACTIVATED);

    public AbstractActivatedPowerSource(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    protected void tick(World world, BlockPos pos, BlockState state){
        //tickCounter = (tickCounter+1)%Integer.MAX_VALUE-1;
        tickCounter++;
        if(tickCounter==Integer.MAX_VALUE){
            tickCounter=0;
        }
        if(tickCounter % 20 == 0 && state.get(AbstractActivatedBlock.ACTIVATED)){
            if(!(path == null))lastPath = new ArrayList<>(path);


            path = BlockHelpers.BlockBFS(world, DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE, DiabolismBlocks.CRYSTAL_ALTAR, pos, null);

            if(!path.isEmpty()){

                for(int i = 1; i < path.size()-1; i++){
                    BlockPos pathPos = path.get(i);
                    RunedBlackstoneBlockEntity runedBlackstoneBlockEntity = (RunedBlackstoneBlockEntity) world.getBlockEntity(pathPos.down());
                    if(runedBlackstoneBlockEntity != null){
                        runedBlackstoneBlockEntity.markActivated();
                    }
                }
                BlockPos altarPos = path.get(path.size()-1);
                AbstractFluidAltar fluidAltar = (AbstractFluidAltar) world.getBlockEntity(altarPos);

                if(fluidAltar != null){
                    System.out.println("marking fluid altar as activated: "+altarPos.toShortString());
                    BlockState altarState = world.getBlockState(altarPos);
                    fluidAltar.markActivated();
                    fluidAltar.addConnectedEntity(pos);
                    world.updateListeners(altarPos, altarState, altarState, Block.NOTIFY_LISTENERS);
                }

            }
            //else System.out.println("path list is empty");
            playParticles(world);
        }
    }

    private void playParticles(World world){
        if(!world.isClient && path != null && !path.isEmpty()){
            if(!path.equals(lastPath)){
                System.out.println("recalculating particle positions");
                particlePositions = new ArrayList<>();
                for(int i = 1; i < path.size()-1; i++){

                    BlockPos pathPos = path.get(i);
                    BlockPos prevDirectionBP = path.get(i-1).subtract(pathPos);
                    Vec3d prevDirection = new Vec3d(prevDirectionBP.getX(), prevDirectionBP.getY(), prevDirectionBP.getZ());

                    BlockPos nextDirectionBP = path.get(i+1).subtract(pathPos);
                    Vec3d nextDirection = new Vec3d(nextDirectionBP.getX(), nextDirectionBP.getY(), nextDirectionBP.getZ());

                    float numParticles = 7;

                    for (int j = 0; j <= numParticles; j++){
                        Vec3d particlePos = new Vec3d(pathPos.getX()+0.5f, pathPos.getY()+0.5f, pathPos.getZ()+0.5f).add(prevDirection.multiply(((double)j)/numParticles).multiply(0.5f));

                        particlePositions.add(particlePos);
//                        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
//                                DiabolismParticles.PROJECTILE_SPELL_PARTICLE, true, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1,
//                                0, 0, 0, 0));
                    }
                    for (int j = 0; j <= numParticles; j++){
                        Vec3d particlePos = new Vec3d(pathPos.getX()+0.5f, pathPos.getY()+0.5f, pathPos.getZ()+0.5f).add(nextDirection.multiply(((double)j)/numParticles).multiply(0.5f));

                        particlePositions.add(particlePos);
//                        PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
//                                DiabolismParticles.PROJECTILE_SPELL_PARTICLE, true, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1,
//                                0, 0, 0, 0));
                    }
                }
            }
            for(Vec3d particlePos : particlePositions){
                PlayerLookup.tracking(this).forEach(player -> ((ServerWorld) world).spawnParticles(player,
                        ColoredSpellParticleFactory.createData(0xFFFFFF, (int) (Math.random() * 8 + 2)), true, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 1,
                        0, 0, 0, 0));
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
