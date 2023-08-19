package jipthechip.diabolism.render;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CuboidRenderData implements Serializable {

    private String[] texturePaths;
    private int color;
    private int luminosity;

    private int opacityOffset;

    private String name;

    public CuboidRenderData(String name, int color, int luminosity, int opacityOffset, String ... texturePaths) {
        this.texturePaths = texturePaths;
        this.color = color;
        this.luminosity = luminosity;
        this.opacityOffset = opacityOffset;
        this.name = name;
    }

    public SpriteIdentifier getTexture(int i) {
        return new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(texturePaths[i]));
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color){ this.color = color;}

    public int getLuminosity() {
        return luminosity;
    }

    public int getOpacityOffset() {
        return opacityOffset;
    }

    public String getCuboidName(){ return name;}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CuboidRenderData && Objects.equals(((CuboidRenderData) obj).name, name);
    }
}
