package jipthechip.diabolism.Utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class ImageUtils {

    // default ingredient color is brown
    private static final Color DEFAULT_INGREDIENT_COLOR = new Color(0xFFA69351, true);

    public static Color getAvgColor(Item item) {

        Identifier itemId = Registries.ITEM.getId(item);

        Identifier textureId = new Identifier(itemId.getNamespace()+":textures/item/"+itemId.getPath()+".png");

        Optional<Resource> resourceOptional = MinecraftClient.getInstance().getResourceManager().getResource(textureId);

        if(resourceOptional.isPresent()){
            try {
                InputStream inputStream = resourceOptional.get().getInputStream();
                BufferedImage image = ImageIO.read(inputStream);
                int numPixels = 0;
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        Color c = new Color(image.getRGB(x, y), true);
                        if(c.getAlpha() > 0){
                            totalRed += c.getRed();
                            totalGreen += c.getGreen();
                            totalBlue += c.getBlue();
                            numPixels++;
                        }
                    }
                }
                Color avgColor = new Color(totalRed/numPixels, totalGreen/numPixels, totalBlue/numPixels, 0xFF);
                return avgColor;

            }catch(IOException e){
                System.out.println("failed to create input stream for '"+textureId+"'");
            }
        }else{
            System.out.println("resource '"+textureId+"' not present");
        }
        return DEFAULT_INGREDIENT_COLOR;
    }

    public static Color getAvgColor(List<Color> colors){
        int totalR = 0, totalG = 0, totalB = 0;

        for(Color color : colors){
            totalR += color.getRed();
            totalG += color.getGreen();
            totalB += color.getBlue();
        }

        return new Color(totalR / colors.size(), totalG / colors.size(), totalB / colors.size());
    }

    public static Color getAvgColor(Color color1, Color color2, float weight){
        return new Color((int) (((color1.getRed() * weight) + color2.getRed()) / (1 + weight)), (int) (((color1.getGreen() * weight) + color2.getGreen()) / (1 + weight)), (int) (((color1.getBlue() * weight) + color2.getBlue()) / (1 + weight)));
    }
}
