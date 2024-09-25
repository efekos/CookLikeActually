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
        registry.addWorkstations(CuttingCategory.ID, EntryStacks.of(ClaBlocks.CUTTING_BOARD));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(CuttingRecipe.class,CuttingRecipe.Type.INSTANCE, CuttingDisplay::new);
    }

}
