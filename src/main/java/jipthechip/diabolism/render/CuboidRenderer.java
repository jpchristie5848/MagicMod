package jipthechip.diabolism.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class CuboidRenderer {


    public static void renderCuboidBER(CuboidRenderData cuboid, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int textureIndex, Vec3d from, Vec3d to, int color){

        from = from.multiply(1.0f/16.0f);
        to = to.multiply(1.0f/16.0f);

        renderCuboid(cuboid, matrices, vertexConsumer, light, overlay, textureIndex, from, to, color);
    }

    public static void renderCuboid(CuboidRenderData cuboid, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int textureIndex, Vec3d from, Vec3d to, int color){
        int combinedLight = (light & 0xFFFF0000) | Math.max(cuboid.getLuminosity() << 4, light & 0xFFFF);
        SpriteIdentifier texture = cuboid.getTexture(0);
        int opacityOffset = cuboid.getOpacityOffset();

        //System.out.println("texture: "+texture);
        //System.out.println("sprite: "+texture.getSprite());

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for(Direction dir : Direction.values()){
            renderQuad(vertexConsumer, matrix, texture, from, to, color, combinedLight, dir, 0, textureIndex > 0 && cuboid instanceof FluidRenderData);
        }
    }

    public static void renderQuad(VertexConsumer vertexConsumer, Matrix4f matrix, SpriteIdentifier texture, Vec3d from, Vec3d to, int color, int light, Direction dir, int rotation, boolean isFlowing){

        float x1 = (float)from.getX(), y1 = (float)from.getY(), z1 = (float)from.getZ();
        float x2 = (float)to.getX(), y2 = (float)to.getY(), z2 = (float)to.getZ();
        Sprite sprite = texture.getSprite();

//        float u1, u2, v1, v2;

//        float minU, maxU, minV, maxV;
//        minU = sprite.getMinU();
//        maxU = sprite.getMaxU();
//        minV = sprite.getMinV();
//        maxV = sprite.getMaxV();
//
//        float u3, u4, v3, v4;
//        u1 = minU; v1 = maxV;
//        u2 = minU; v2 = minV;
//        u3 = maxU; v3 = minV;
//        u4 = maxU; v4 = maxV;

        
        int light1 = light & 0xFFFF;
        int light2 = light >> 16 & 0xFFFF;
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        switch (dir) {
            case DOWN -> {
                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(x2-x1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(z2-z1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;

                vertexConsumer.vertex(matrix, x1, y1, z2).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(0,-1,0).next();
                vertexConsumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(0,-1,0).next();
                vertexConsumer.vertex(matrix, x2, y1, z1).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(0,-1,0).next();
                vertexConsumer.vertex(matrix, x2, y1, z2).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(0,-1,0).next();
            }
            case UP -> {

                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(x2-x1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(z2-z1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;
                vertexConsumer.vertex(matrix, x1, y2, z1).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(0,1,0).next();
                vertexConsumer.vertex(matrix, x1, y2, z2).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(0,1,0).next();
                vertexConsumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(0,1,0).next();
                vertexConsumer.vertex(matrix, x2, y2, z1).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(0,1,0).next();
            }
            case NORTH -> {
                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(x2-x1));//Math.min(sprite.getMaxU(), sprite.getMinU()+Math.abs(z2-z1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(y2-y1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;
                vertexConsumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(0,0,-1).next();
                vertexConsumer.vertex(matrix, x1, y2, z1).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(0,0,-1).next();
                vertexConsumer.vertex(matrix, x2, y2, z1).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(0,0,-1).next();
                vertexConsumer.vertex(matrix, x2, y1, z1).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(0,0,-1).next();
            }
            case SOUTH -> {
                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(x2-x1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(y2-y1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;
                vertexConsumer.vertex(matrix, x2, y1, z2).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(0,0,1).next();
                vertexConsumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(0,0,1).next();
                vertexConsumer.vertex(matrix, x1, y2, z2).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(0,0,1).next();
                vertexConsumer.vertex(matrix, x1, y1, z2).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(0,0,1).next();
            }
            case WEST -> {
                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(z2-z1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(y2-y1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;
                vertexConsumer.vertex(matrix, x1, y1, z2).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(-1,0,0).next();
                vertexConsumer.vertex(matrix, x1, y2, z2).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(-1,0,0).next();
                vertexConsumer.vertex(matrix, x1, y2, z1).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(-1,0,0).next();
                vertexConsumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(-1,0,0).next();
            }
            case EAST -> {
                float u1, u2, v1, v2;

                float minU, maxU, minV, maxV;
                minU = sprite.getMinU();
                maxU = Math.min(sprite.getMaxU(), minU+(sprite.getMaxU()-minU)*Math.abs(z2-z1));
                minV = sprite.getMinV();
                maxV = Math.min(sprite.getMaxV(), minV+(sprite.getMaxV()-minV)*Math.abs(y2-y1));

                float u3, u4, v3, v4;
                u1 = minU; v1 = maxV;
                u2 = minU; v2 = minV;
                u3 = maxU; v3 = minV;
                u4 = maxU; v4 = maxV;
                vertexConsumer.vertex(matrix, x2, y1, z1).color(r, g, b, a).texture(u1, v1).light(light1, light2).normal(1,0,0).next();
                vertexConsumer.vertex(matrix, x2, y2, z1).color(r, g, b, a).texture(u2, v2).light(light1, light2).normal(1,0,0).next();
                vertexConsumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).texture(u3, v3).light(light1, light2).normal(1,0,0).next();
                vertexConsumer.vertex(matrix, x2, y1, z2).color(r, g, b, a).texture(u4, v4).light(light1, light2).normal(1,0,0).next();
            }
        }
    }
}
