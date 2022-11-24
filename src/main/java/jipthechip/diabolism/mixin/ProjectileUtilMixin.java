package jipthechip.diabolism.mixin;

import jipthechip.diabolism.Utils.Dimension;
import jipthechip.diabolism.Utils.MathUtils;
import jipthechip.diabolism.entities.ProjectileSpellEntity;
import jipthechip.diabolism.entities.ShieldSpellEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {

    @Inject(at = @At("HEAD"), method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;")
    private static void diabolism$getEntityCollision(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, float f, CallbackInfoReturnable<EntityHitResult> cir){
        //System.out.println("Secret Box: "+box);
//        System.out.println("bounding box in projectile util: "+ MathUtils.distanceBetween2Points(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
        //System.out.println("Entity Collision Return: "+cir.getReturnValue());
//        System.out.println("Dimensions: "+((EntityAccessor)entity).getDimensions());
//        System.out.println("Min: "+min);
//        System.out.println("Max:" +max);
//        System.out.println("Max - Min"+(max.subtract(min)));
//        System.out.println("-----------------------------------------");

//        System.out.println("Bounding box size in getEntityCollision: "+MathUtils.distanceBetween2Points(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
//        if(!entity.getEntityWorld().isClient) {
//            if(entity instanceof ProjectileSpellEntity){
//                Box finalBox = ((ProjectileSpellEntity)entity).calculateBoundingBox();;
//                PlayerLookup.tracking(entity).forEach(player -> {
//                    ((ServerWorld) world).spawnParticles(player,
//                            ParticleTypes.FLAME, true, finalBox.minX, finalBox.minY, finalBox.minZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.maxY, finalBox.maxZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.maxY, finalBox.minZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.minY, finalBox.minZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.minY, finalBox.maxZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.maxY, finalBox.maxZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.maxX, finalBox.minY, finalBox.maxZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, finalBox.minX, finalBox.maxY, finalBox.minZ, 1,
//                            0, 0, 0, 0);
//                    ((ServerWorld) world).spawnParticles(player, ParticleTypes.FLAME, true, entity.getPos().x, entity.getPos().y, entity.getPos().z, 1,
//                            0, 0, 0, 0);
//                });
//            }
//        }
    }

    @Inject(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At("TAIL"), locals= LocalCapture.CAPTURE_FAILHARD)
    private static void diabolism$getEntityCollisionReturn(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, float f, CallbackInfoReturnable<EntityHitResult> cir, double d, Entity entity2){
        System.out.println("entity: "+entity.getClass());
        System.out.println("entity2: "+entity2.getClass());
        if(entity2 instanceof ShieldSpellEntity && entity instanceof ProjectileSpellEntity){
            Vec3d newVelocity = MathUtils.rodriguesRotation(entity.getVelocity().multiply(-1), ((ShieldSpellEntity)entity2).getPlayerLookVector(), 180);
            ((ProjectileSpellEntity)entity).setRealVelocity(newVelocity);
//            System.out.println("set velocity to "+newVelocity+" in get entity collision");
        }
    }

    @ModifyVariable(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At("STORE"), ordinal = 0)
    private static Optional<Vec3d> diabolism$optional(Optional<Vec3d> x, World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, float f) {
        if(entity instanceof ProjectileSpellEntity){
            //System.out.println("Optional is present for new raycast: "+projectileSpellraycast(box, min, max));
            return projectileSpellraycast(((ProjectileSpellEntity)entity).calculateBoundingBox(), min, max);
        }else{
            return x;
        }
    }

    private static Optional<Vec3d> projectileSpellraycast(Box box, Vec3d min, Vec3d max) {
        double d = max.x - min.x;
        double e = max.y - min.y;
        double f = max.z - min.z;
//        System.out.println("e: "+e+" box delta:"+Math.abs((box.minY-box.maxY)));
        double[] ds = new double[]{Math.abs(((box.minX-box.maxX)/2) / d), Math.abs(((box.minY-box.maxY)/2) / e), Math.abs(((box.minZ-box.maxZ)/2) / f)};

//        System.out.println("Initial ds: "+Arrays.toString(ds));
        Direction direction = traceCollisionSide(box, min, ds, (Direction)null, d, e, f);
        if (direction == null) {
            return Optional.empty();
        } else {
            double g = Collections.min(Arrays.asList(ArrayUtils.toObject(ds)));
            return Optional.of(min.add(g * d, g * e, g * f));
        }
    }

    @Nullable
    private static Direction traceCollisionSide(Box box, Vec3d intersectingVector, double[] traceDistanceResult, @Nullable Direction approachDirection, double deltaX, double deltaY, double deltaZ) {
        if (deltaX > 1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaX, deltaY, deltaZ, box.minX, box.minY, box.maxY, box.minZ, box.maxZ, Direction.WEST, intersectingVector.x, intersectingVector.y, intersectingVector.z, Dimension.X);
        } else if (deltaX < -1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaX, deltaY, deltaZ, box.maxX, box.minY, box.maxY, box.minZ, box.maxZ, Direction.EAST, intersectingVector.x, intersectingVector.y, intersectingVector.z, Dimension.X);
        }

        if (deltaY > 1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaY, deltaZ, deltaX, box.minY, box.minZ, box.maxZ, box.minX, box.maxX, Direction.DOWN, intersectingVector.y, intersectingVector.z, intersectingVector.x, Dimension.Y);
        } else if (deltaY < -1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaY, deltaZ, deltaX, box.maxY, box.minZ, box.maxZ, box.minX, box.maxX, Direction.UP, intersectingVector.y, intersectingVector.z, intersectingVector.x, Dimension.Y);
        }

        if (deltaZ > 1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaZ, deltaX, deltaY, box.minZ, box.minX, box.maxX, box.minY, box.maxY, Direction.NORTH, intersectingVector.z, intersectingVector.x, intersectingVector.y, Dimension.Z);
        } else if (deltaZ < -1.0E-7D) {
            approachDirection = traceCollisionSide(traceDistanceResult, approachDirection, deltaZ, deltaX, deltaY, box.maxZ, box.minX, box.maxX, box.minY, box.maxY, Direction.SOUTH, intersectingVector.z, intersectingVector.x, intersectingVector.y, Dimension.Z);
        }

        //System.out.println("collision from "+approachDirection);
        return approachDirection;
    }

    @Nullable
    private static Direction traceCollisionSide(double[] traceDistanceResult, @Nullable Direction approachDirection, double deltaX, double deltaY, double deltaZ, double begin, double minX, double maxX, double minZ, double maxZ, Direction resultDirection, double startX, double startY, double startZ, Dimension dim) {

        double d = Math.abs((begin - startX) / deltaX);

        double e = startY + d * deltaY;
        double f = startZ + d * deltaZ;

//        System.out.println("checking dimension "+dim.name());
//        System.out.println("d: "+d+" traceDistResult: "+traceDistanceResult[dim.ordinal()]);
//        System.out.println("e: "+e+" minx: "+minX+" maxX: "+maxX);
//        System.out.println("f: "+f+" minz: "+minZ+" maxZ: "+maxZ);

        if (0.0D < d && d <= traceDistanceResult[dim.ordinal()] && minX - 1.0E-7D < e && e < maxX + 1.0E-7D && minZ - 1.0E-7D < f && f < maxZ + 1.0E-7D) {
            traceDistanceResult[dim.ordinal()] = d;
//            System.out.println("Set traceDistanceResult to "+d+" for "+dim.name()+" dimension");
            return resultDirection;
        } else {
            return approachDirection;
        }
    }
//    @ModifyVariable(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At("STORE"), ordinal = 1, argsOnly = false)
//    private static Box diabolism$box(Box box) {
//        System.out.println("bounding box after expansion in projectile util: "+MathUtils.distanceBetween2Points(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
//        return box;
//    }
//
//    @ModifyVariable(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At("STORE"), ordinal = 0, argsOnly = false)
//    private static Iterator diabolism$iterator(Iterator iterator) {
//        return iterator;
//    }

    // method = Lnet/minecraft/util/EntityHitResult;getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate<Lnet/minecraft/entity/Entity;>;F)
}
