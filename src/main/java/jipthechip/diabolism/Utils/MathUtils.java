package jipthechip.diabolism.Utils;

import net.minecraft.util.math.Vec3d;

import static java.lang.Math.sin;
import static net.minecraft.util.math.MathHelper.cos;

public class MathUtils {

    public static Vec3d PitchAndYawToVec3d(float pitch, float yaw){

        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);

        double sinPitch = Math.sin(pitchRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinYaw = Math.sin(yawRadians);
        double cosYaw = Math.cos(yawRadians);

        return new Vec3d(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
    }

    public static Vec3d getPointOnSphere(float pitch, float yaw, float radius, Vec3d centerPos){
        return PitchAndYawToVec3d(pitch, yaw).multiply(radius).add(centerPos);
    }

    public static double distanceBetween2Points(double x1, double y1, double z1, double x2, double y2, double z2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) + Math.pow(z2-z1, 2));
    }

    public static Vec3d rotateVectorAboutAnotherVector(Vec3d vec1, Vec3d vec2, double angle){
        // component of vec1 parallel to vec2
        Vec3d parVec = vec2.multiply(vec1.dotProduct(vec2)/vec2.dotProduct(vec2));
        // component of vec1 perpendicular to vec2
        Vec3d perpVec = vec1.subtract(parVec);
        // vector perpendicular to vec2 and perpVec
        Vec3d perpVec2 = vec2.crossProduct(perpVec);
        // rotation of perpVec
        Vec3d perpRotVec = perpVec.multiply(Math.cos(Math.toRadians(angle))/perpVec.length()).add(perpVec2.multiply(Math.sin(Math.toRadians(angle))/perpVec2.length())).multiply(perpVec.length());

        // rotation of vec1 about vec2 by [angle] degrees
        return perpRotVec.add(parVec);
    }

    public static Vec3d rodriguesRotation(Vec3d v, Vec3d k, double angle){
        double radians = Math.toRadians(angle);
        Vec3d part1 = v.multiply(Math.cos(radians)); // v cos0
        Vec3d part2 = k.crossProduct(v).multiply(Math.sin(radians)); // (k x v) sin0
        Vec3d part3 = k.multiply(k.dotProduct(v)).multiply(1 - Math.cos(radians)); // k(k * v)(1 - cos0)

        return part1.add(part2).add(part3);
    }

    public static Vec3d getPerpendicularToLookVector(Vec3d lookVector){
        double x = 1;
        double y = 0;
        double z = (lookVector.x * -1) / lookVector.z;

        return new Vec3d(x, y ,z).normalize();
    }
}
