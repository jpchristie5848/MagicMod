package jipthechip.diabolism.entities.blockentities.screen;

import jipthechip.diabolism.data.MagickaFluid;
import jipthechip.diabolism.data.Spell;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ArcaneAltarScreen extends HandledScreen<ArcaneAltarScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("diabolism", "textures/ui/container/arcane_altar.png");
    private static final int BAR_HEIGHT = 74;
    private static final int BAR_WIDTH = 16;
    private static final int BAR_START_X = 56;
    private static final int BAR_START_Y = 118;


    public ArcaneAltarScreen(ArcaneAltarScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 237;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TEXTURE);
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 10;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        //drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(context, x, y);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //System.out.println("called screen render");

        renderBackground(context, mouseX, mouseY,delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        renderMagickaBar(context, x, y, mouseX, mouseY);

    }

    private void renderMagickaBar(DrawContext context, int x, int y, int mouseX, int mouseY){
        List<MagickaFluid> fluids = this.handler.getMagickaFluids();

        int heightOffset = 0;

        int segmentStartX = x+ BAR_START_X;
        int segmentEndX = x+ BAR_START_X +BAR_WIDTH;

        for(int i = 0; i < fluids.size(); i++){

            MagickaFluid fluid = fluids.get(i);

            // makes sure all the individual bars' heights add up to the total height of the bar when it's full
            int totalSegmentHeightOffset = i % 2 == 0 ? -1 : 0;

            int totalHeight = (int)Math.ceil(BAR_HEIGHT * (fluid.getAmount()/handler.getContainerCapacity())) + totalSegmentHeightOffset;

            int pureHeight = (int)Math.ceil(totalHeight * fluid.getPurity());


            int pureSegmentStartY = y+ BAR_START_Y -heightOffset-pureHeight;
            int pureSegmentEndY = y+ BAR_START_Y -heightOffset;

            // draw pure segment
            context.fill(segmentStartX, pureSegmentStartY, segmentEndX, pureSegmentEndY, 0,  Spell.ELEMENT_COLORS[fluid.getElement().ordinal()]);

            heightOffset += pureHeight;

            if(isInBox(mouseX, mouseY, segmentStartX, pureSegmentStartY, segmentEndX, pureSegmentEndY)){

                List<Text> toolTipTexts = new ArrayList<>();

                float purity = (fluid.getPurity())/fluids.size();

                toolTipTexts.add(MutableText.of(new LiteralTextContent("Element: "+fluid.getElement().name())));
                toolTipTexts.add(MutableText.of(new LiteralTextContent("Purity: "+(purity*100)+"%")));
                toolTipTexts.add(MutableText.of(new LiteralTextContent("Amount: "+(fluid.getAmount()*fluid.getPurity())+" mB")));

                context.drawTooltip(this.textRenderer, toolTipTexts, x+ BAR_START_X, y+ BAR_START_Y -heightOffset-pureHeight);
            }
        }

        int impureSegmentStartY = y+ BAR_START_Y - BAR_HEIGHT + (int)Math.floor((handler.getRemainingSpace()/handler.getContainerCapacity())*BAR_HEIGHT);

        // fill any empty space left at the top if the bar isn't totally full
        if(this.handler.getRemainingSpace() == 0 && impureSegmentStartY > BAR_START_Y){
            impureSegmentStartY = BAR_START_Y;
        }

        context.fill(segmentStartX, impureSegmentStartY, segmentEndX, y+ BAR_START_Y -heightOffset, 0, 0xff333333);
        context.getMatrices().pop();
    }

    private void renderProgressArrow(DrawContext context, int x, int y){
        if(handler.isCrafting() && handler.getScaledProgress() > 0){
            System.out.println("scaled progress: "+handler.getScaledProgress());
            context.drawTexture(TEXTURE, x+119, y+79, 176, 0, handler.getScaledProgress(), 11);
        }
    }

    private static boolean isInBox(int x, int y, int x1, int y1, int x2, int y2){
        return (x >= x1 && x < x2 && y >= y1 && y < y2);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title))/2;
        titleY = 5;
        playerInventoryTitleY = 147;
    }

    private static int changeColorLightness(int color, float factor){
        int a = (color & 0xff000000) >> 24;
        int r = (color & 0xff0000) >> 16;
        int g = (color & 0xff00) >> 8;
        int b = color & 0xff;

        r = Math.min((int)(r * factor), 0xff);
        g = Math.min((int)(g * factor), 0xff);
        b = Math.min((int)(b * factor), 0xff);

        int result = ((a << 24) | (r << 16) | (g << 8) | b);

        return result;
    }

    /*
    List<MagickaFluid> fluids = this.handler.getMagickaFluids();

        int heightOffset = 0;

        int drawnBarHeight = 0;

        System.out.println("remaining space: "+this.handler.getRemainingSpace());

        for(int i = 0; i < fluids.size(); i++){

            MagickaFluid fluid = fluids.get(i);


            // makes sure all the individual bars' heights add up to the total height of the bar when it's full
            int totalSegmentHeightOffset = i % 2 == 0 ? -1 : 0;

            int totalHeight = (int)Math.ceil(BAR_HEIGHT * (fluid.getAmount()/handler.getContainerCapacity())) + totalSegmentHeightOffset;

            drawnBarHeight += totalHeight;

            // fill any empty space left at the top if the bar isn't totally full
            if(this.handler.getRemainingSpace() == 0 && i == fluids.size() - 1 && drawnBarHeight < BAR_HEIGHT){
                totalHeight += BAR_HEIGHT - drawnBarHeight;
            }


            int pureHeight = (int)Math.ceil(totalHeight * fluid.getPurity());


            int segmentStartX = x+ BAR_START_X;
            int pureSegmentStartY = y+ BAR_START_Y -heightOffset-pureHeight;
            int segmentEndX = x+ BAR_START_X +BAR_WIDTH;
            int pureSegmentEndY = y+ BAR_START_Y -heightOffset;

            // draw pure segment
            context.fill(segmentStartX, pureSegmentStartY, segmentEndX, pureSegmentEndY, 0,  Spell.ELEMENT_COLORS[fluid.getElement().ordinal()]);

            heightOffset += pureHeight;

            int impureSegmentStartY = y+ BAR_START_Y -heightOffset-(totalHeight-pureHeight);
            int impureSegmentEndY = y+ BAR_START_Y -heightOffset;

            // draw impure segment
            context.fill(segmentStartX, impureSegmentStartY, segmentEndX, impureSegmentEndY, 0, changeColorLightness(Spell.ELEMENT_COLORS[fluid.getElement().ordinal()], 0.5f));

            heightOffset += totalHeight-pureHeight;

            if(isInBox(mouseX, mouseY, segmentStartX, impureSegmentStartY, segmentEndX, pureSegmentEndY)){

                List<Text> toolTipTexts = new ArrayList<>();

                toolTipTexts.add(MutableText.of(new LiteralTextContent("Element: "+fluid.getElement().name())));
                toolTipTexts.add(MutableText.of(new LiteralTextContent("Purity: "+(fluid.getPurity()*100)+"%")));
                toolTipTexts.add(MutableText.of(new LiteralTextContent("Amount: "+fluid.getAmount()+" mB")));

                context.drawTooltip(this.textRenderer, toolTipTexts, x+ BAR_START_X, y+ BAR_START_Y -heightOffset-pureHeight);
            }
        }
        context.getMatrices().pop();
     */
}
