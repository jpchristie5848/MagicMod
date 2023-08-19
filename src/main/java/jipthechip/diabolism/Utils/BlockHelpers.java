package jipthechip.diabolism.Utils;

import jipthechip.diabolism.entities.blockentities.AbstractFluidContainer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class BlockHelpers {

    public static List<BlockPos> PipeBFS(World world, BlockPos start, BlockPos pumpPos, TargetCondition targetCondition, List<BlockPos> ignoredTargets, Block... pipeBlocks){

        List<Block> pipeBlocklist = Arrays.stream(pipeBlocks).toList();
        List<BlockPos> path = new ArrayList<>();

        HashMap<BlockPos, BlockPos> parents = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();

        queue.push(start);
        while (!queue.isEmpty()){

//            System.out.print("inside bfs: ");
//            for(BlockPos qpos: queue){
//                System.out.print(qpos.toShortString()+";");
//            }
//            System.out.println();
            BlockPos currentPos = queue.removeFirst();

//            System.out.println("current pos: "+currentPos.toShortString()+"---------------");
            // check all 4 directions for a path
            for(Direction direction : Direction.values()){

                BlockPos newPos = currentPos.add(direction.getVector());

                if(!queue.contains(newPos) && !newPos.equals(start)){
                    //System.out.println("checking "+newPos.toShortString());

                    Block block = world.getBlockState(newPos).getBlock();

                    if(!ignoredTargets.contains(newPos) && targetCondition.evaluate(world.getBlockEntity(newPos))){
                        path.add(newPos);
                        parents.put(newPos, currentPos);
                        do{
                            path.add(parents.get(path.get(path.size()-1)));
                        }while(path.get(path.size()-1) != start);

                        Collections.reverse(path);

                        if(path.get(1).equals(pumpPos))
                            return path;
                        else{ // if the path is going through a different pump other than the one that called this method, it is invalid
                            path.clear();
                        }
                    }
                    else if(pipeBlocklist.contains(block) && !parents.containsKey(newPos)){
                        queue.addLast(newPos);
                        parents.put(newPos, currentPos);
                    }
                }
            }
        }
//        System.out.println("no path found");
//        System.out.println();
        return path;
    }

    public static List<BlockPos> BlockBFS(World world, Block pathBlock, Block targetBlock, BlockPos start, @Nullable TargetCondition targetCondition){

        if(world.getBlockState(start).getBlock() == targetBlock){
            return Collections.singletonList(start);
        }

        List<BlockPos> path = new ArrayList<>();

        HashMap<BlockPos, BlockPos> parents = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();

        queue.push(start);
        while (!queue.isEmpty()){

//            System.out.print("inside bfs: ");
//            for(BlockPos qpos: queue){
//                System.out.print(qpos.toShortString()+";");
//            }
//            System.out.println();
            BlockPos currentPos = queue.removeFirst();

//            System.out.println("current pos: "+currentPos.toShortString()+"---------------");
            // check all 4 directions for a path
            for(Direction direction : Direction.Type.HORIZONTAL){

                BlockPos newPos = currentPos.add(direction.getVector());

                if(!queue.contains(newPos)){
                    //System.out.println("checking "+newPos.toShortString());

                    Block block = world.getBlockState(newPos).getBlock();
                    Block downBlock = world.getBlockState(newPos.down()).getBlock();

                    if(block.equals(targetBlock) && (targetCondition == null || targetCondition.evaluate(world.getBlockEntity(newPos)))){
                        path.add(newPos);
                        parents.put(newPos, currentPos);
                        do{
                            path.add(parents.get(path.get(path.size()-1)));
                        }while(path.get(path.size()-1) != start);

                        Collections.reverse(path);
                        return path;
                    }
                    else if(block.equals(Blocks.AIR) && downBlock.equals(pathBlock) && !(parents.containsKey(newPos) || newPos == start)){
                        queue.addLast(newPos);
                        parents.put(newPos, currentPos);
                    }
                }
            }
        }
//        System.out.println("no path found");
//        System.out.println();
        return path;
    }
}
