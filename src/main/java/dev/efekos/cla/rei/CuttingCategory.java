package dev.efekos.cla.rei;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.rei.widget.KnifeWidget;
import me.shedaniel.math.Point;
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

public class CuttingCategory implements DisplayCategory<CuttingDisplay> {

    public static final Identifier ID = Identifier.of(Main.MOD_ID, "cutting");
    public static final CategoryIdentifier<CuttingDisplay> CATEGORY_ID = CategoryIdentifier.of(ID);

    @Override
    public CategoryIdentifier<? extends CuttingDisplay> getCategoryIdentifier() {
        return CATEGORY_ID;
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

    @Override
    public List<Widget> setupDisplay(CuttingDisplay display, me.shedaniel.math.Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point center = new Point(bounds.getCenterX(), bounds.getCenterY());
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(center.x - 40, center.y - 8)).entries(display.getInputEntries().getFirst()).markInput());
        widgets.add(Widgets.createSlot(new Point(center.x + 24, center.y - 8)).entries(display.getOutputEntries().getFirst()).markInput());
        widgets.add(new KnifeWidget(new Point(center.x - 16, center.y - 5)).maxCuts(display.getCuts()));
        return widgets;
    }

}
