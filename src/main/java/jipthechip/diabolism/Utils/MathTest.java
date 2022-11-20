package jipthechip.diabolism.Utils;

import net.minecraft.util.math.Vec3d;

public class MathTest {

    public static void main(String[] args) {
        Vec3d vec1 = new Vec3d(3,1,-4);
        Vec3d vec2 = new Vec3d(-2,3,3);
        System.out.println(MathUtils.rotateVectorAboutAnotherVector(vec1, vec2, -90));

        System.out.println(MathUtils.rodriguesRotation(vec1, vec2, 90));

        System.out.println(new Vec3d(1, 0, -0.75).normalize());
    }
}
