package jipthechip.diabolism.mixin;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.class)
public interface SpriteAccessor {


    @Accessor("contents")
    public SpriteContents getSpriteContents();

}
