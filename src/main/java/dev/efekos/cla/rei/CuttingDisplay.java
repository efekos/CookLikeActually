package dev.efekos.cla.rei;

import dev.efekos.cla.recipe.CuttingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.RecipeEntry;

import java.util.Arrays;
import java.util.List;

public class CuttingDisplay extends BasicDisplay {

    private int cuts;

    public CuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int cuts) {
        super(inputs, outputs);
        this.cuts = cuts;
    }

    public CuttingDisplay(RecipeEntry<CuttingRecipe> entry) {
        this(List.of(
                        EntryIngredient.of(Arrays.stream(entry.value().getItem().getMatchingStacks()).map(EntryStacks::of).toList())),
                List.of(EntryIngredient.of(EntryStacks.of(entry.value().getRes()))), entry.value().getCuts());
    }

    public CuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CuttingCategory.ID;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return super.getInputEntries();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return super.getOutputEntries();
    }

    public int getCuts() {
        return cuts;
    }

}
