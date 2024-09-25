package dev.efekos.cla.rei;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CuttingCategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<BasicDisplay> ID = CategoryIdentifier.of(Main.MOD_ID,"cutting");
    private Identifier identifier;

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.cla.cutting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ClaBlocks.CUTTING_BOARD);
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }
    private static final Identifier TEXTURE = Identifier.of(Main.MOD_ID, "textures/gui/rei/cutting.png");


    @Override
    public List<Widget> setupDisplay(BasicDisplay display, me.shedaniel.math.Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point point = new Point(bounds.getCenterX()-(64/2), bounds.getCenterY()-(36/2));
        widgets.add(Widgets.createTexturedWidget(TEXTURE,point.x,point.y,64,32));
        widgets.add(Widgets.createSlot(new Point(point.x+5,point.y+8)).entries(display.getInputEntries().getFirst()).markInput());
        widgets.add(Widgets.createSlot(new Point(point.x+37,point.y+9)).entries(display.getOutputEntries().getFirst()).markInput().disableBackground());
        return widgets;
    }

}
