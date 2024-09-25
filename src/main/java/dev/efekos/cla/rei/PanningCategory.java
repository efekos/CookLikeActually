package dev.efekos.cla.rei;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PanningCategory implements DisplayCategory<ArrowProgressDisplay> {

    public static final Identifier ID = Identifier.of(Main.MOD_ID,"panning");
    public static final CategoryIdentifier<ArrowProgressDisplay> CATEGORY_ID = CategoryIdentifier.of(ID);

    @Override
    public CategoryIdentifier<? extends ArrowProgressDisplay> getCategoryIdentifier() {
        return CATEGORY_ID;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.cla.panning");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ClaBlocks.PAN);
    }

    @Override
    public List<Widget> setupDisplay(ArrowProgressDisplay display, Rectangle bounds) {
        ArrayList<Widget> widgets = new ArrayList<>();
        Point center = new Point(bounds.getCenterX(), bounds.getCenterY());
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(center.x-40,center.y-8)).entries(display.getInputEntries().getFirst()).markInput());
        widgets.add(Widgets.createSlot(new Point(center.x+24,center.y-8)).entries(display.getOutputEntries().getFirst()).markOutput());
        widgets.add(Widgets.createArrow(new Point(center.x-12, center.y-8)).animationDurationTicks(display.getTime()));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

}