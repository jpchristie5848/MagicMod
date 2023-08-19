package jipthechip.diabolism.entities.blockentities;

import jipthechip.diabolism.Utils.BlockHelpers;
import jipthechip.diabolism.Utils.TargetCondition;
import jipthechip.diabolism.blocks.DiabolismBlocks;
import jipthechip.diabolism.entities.DiabolismEntities;
import jipthechip.diabolism.entities.MagickaParticleEntity;
import jipthechip.diabolism.render.RenderDataMappings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractMagickaCollector extends BlockEntity {

    int tickCounter = 0;
    List<BlockPos> path;

    String magickaType;

    private final TargetCondition CAN_ADD_FLUID = (blockEntity) -> {
        AbstractFluidContainer container = (AbstractFluidContainer) blockEntity;
        return (container.getFluidRenderData() == null || container.getFluidRenderData() == RenderDataMappings.Fluids.get(magickaType)) && !container.isFull();
    };

    public AbstractMagickaCollector(BlockEntityType<?> type, BlockPos pos, BlockState state, String magickaType) {
        super(type, pos, state);
        this.magickaType = magickaType;
    }

    public static void ticker(World world, BlockPos pos, BlockState state, AbstractMagickaCollector be) {
        be.tick(world, pos, state);
    }

    private void tick(World world, BlockPos pos, BlockState state){
        tickCounter++;
        if(tickCounter % 80 == 0 && tickConditionsMet()){
            path = BlockHelpers.BlockBFS(world, DiabolismBlocks.RUNED_DOUBLE_POLISHED_BLACKSTONE, DiabolismBlocks.CRYSTAL_ALTAR, pos, CAN_ADD_FLUID);
            AbstractFluidContainer container = path.isEmpty() ? null : (AbstractFluidContainer) world.getBlockEntity(path.get(path.size()-1));
            if(!path.isEmpty() && container != null && (container.getFluidRenderData() == null || container.getFluidRenderData() == RenderDataMappings.Fluids.get(magickaType)) && !container.isFull()){
                //System.out.println("time of day: "+timeOfDay);
                int particlePotency = calculatePotency();

                if(path.size() <= 2){

                }else {
                    float weight = 1.7f;
                    BlockPos firstPathPos = path.get(1);
                    // weighted average of collector pos and first path pos, favoring first path pos so particle doesn't spawn on top of collector and cause lag
                    Vec3d startPos = new Vec3d((pos.getX()+(firstPathPos.getX()*weight))/(1+weight), (pos.getY()+(firstPathPos.getY()*weight))/(1+weight), (pos.getZ()+(firstPathPos.getZ()*weight))/(1+weight));

                    //System.out.println("spawning magicka particle with potency "+particlePotency);
                    startPos = startPos.add(0.5, 0.5, 0.5);

                    Vec3d startVelocity = new Vec3d(path.get(1).getX()-pos.getX(), 0, path.get(1).getZ()-pos.getZ()).normalize().multiply(0.05);
                    MagickaParticleEntity entity = new MagickaParticleEntity(DiabolismEntities.MAGICKA_PARTICLE, world, magickaType, particlePotency, startPos, startVelocity);
                    world.spawnEntity(entity);
                }
            }
        }
    }

    protected abstract int calculatePotency();

    protected abstract boolean tickConditionsMet();


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
