package dev.efekos.cla.rei;

import dev.efekos.cla.recipe.FryingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.RecipeEntry;

import java.util.List;

public class FryingDisplay extends BasicDisplay {

    private final int time;

    public FryingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int time) {
        super(inputs, outputs);
        this.time = time;
    }

    public FryingDisplay(RecipeEntry<FryingRecipe> entry) {
        this(List.of(EntryIngredient.of(EntryStacks.of(entry.value().getItem().getMatchingStacks()[0]))),List.of(EntryIngredient.of(EntryStacks.of(entry.value().getRes()))),entry.value().getTime());
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FryingCategory.CATEGORY_ID;
    }

    public int getTime() {
        return time;
    }

}
