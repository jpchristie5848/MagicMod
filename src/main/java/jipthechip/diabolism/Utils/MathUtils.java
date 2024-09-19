package jipthechip.diabolism.Utils;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

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
        return PitchAndYawToVec3d(pitch, yaw).normalize().multiply(radius).add(centerPos);
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

    // rotates vector v about vector k by the specified number of degrees
    public static Vec3d rodriguesRotation(Vec3d v, Vec3d k, double degrees){
        Vec3d v_norm = v.normalize();
        Vec3d k_norm = k.normalize();

        double radians = Math.toRadians(degrees);
        Vec3d part1 = v_norm.multiply(Math.cos(radians)); // v cos0
        Vec3d part2 = k_norm.crossProduct(v_norm).multiply(Math.sin(radians)); // (k x v) sin0
        Vec3d part3 = k_norm.multiply(k_norm.dotProduct(v_norm)).multiply(1 - Math.cos(radians)); // k(k * v)(1 - cos0)

        return part1.add(part2).add(part3).multiply(v.length()); //(v cos0)+((k x v) sin0)+(k(k * v)(1 - cos0))
    }

    public static Vec3d getPerpendicularToLookVector(Vec3d lookVector){
        double x = 1;
        double y = 0;
        double z = (lookVector.x * -1) / lookVector.z;

        return new Vec3d(x, y ,z).normalize();
    }

    public static int getNthGreatestValue(float[] arr, int n){

        List<Integer> indicesToSkip = new ArrayList<>();

        int maxIndex = -1;
        for(int i = 0; i < n; i++){
            maxIndex = -1;
            for(int j = 0; j < arr.length; j++) {
                if(!indicesToSkip.contains(j) && (maxIndex == -1 || arr[j] > arr[maxIndex])){
                    maxIndex = j;
                }
            }
            indicesToSkip.add(maxIndex);
        }
        return maxIndex;
    }

    public static float[] removeNthElementFromArray(float[] arr, int n){
        float[] newArr = new float[arr.length-1];
        int k = 0;
        for(int i = 0; i < arr.length; i++){
            if(i != n){
                newArr[k] = arr[i];
                k++;
            }
        }
        return newArr;
    }

    public static float roundToDecimalPlace(double value, int places) {
        double scale = Math.pow(10, places);       // Scale factor to shift the decimal
        return (float) (Math.round(value * scale) / scale);  // Round and then shift back
    }
}
