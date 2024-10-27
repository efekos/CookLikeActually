package dev.efekos.cla.rei;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.recipe.CuttingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class ClaRei implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CuttingCategory());
        registry.addWorkstations(CuttingCategory.CATEGORY_ID, EntryStacks.of(ClaBlocks.CUTTING_BOARD));
        registry.add(new FryingCategory());
        registry.addWorkstations(FryingCategory.CATEGORY_ID, EntryStacks.of(ClaBlocks.FRYING_STAND));
        registry.add(new PanningCategory());
        registry.addWorkstations(PanningCategory.CATEGORY_ID, EntryStacks.of(ClaBlocks.PAN));
        registry.add(new PottingCategory());
        registry.addWorkstations(PottingCategory.CATEGORY_ID, EntryStacks.of(ClaBlocks.POT));
    }

}
