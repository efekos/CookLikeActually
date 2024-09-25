package dev.efekos.cla.rei;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.recipe.CuttingRecipe;
import dev.efekos.cla.recipe.FryingRecipe;
import dev.efekos.cla.recipe.PanningRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class ClaRei implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CuttingCategory());
        registry.addWorkstations(CuttingCategory.ID, EntryStacks.of(ClaBlocks.CUTTING_BOARD));
        registry.add(new FryingCategory());
        registry.addWorkstations(FryingCategory.CATEGORY_ID, EntryStacks.of(ClaBlocks.FRYING_STAND));
        registry.add(new PanningCategory());
        registry.addWorkstations(PanningCategory.CATEGORY_ID,EntryStacks.of(ClaBlocks.PAN));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(CuttingRecipe.class,CuttingRecipe.Type.INSTANCE, CuttingDisplay::new);
        registry.registerRecipeFiller(FryingRecipe.class,FryingRecipe.Type.INSTANCE, entry -> new ArrowProgressDisplay(FryingCategory.CATEGORY_ID,entry.value()));
        registry.registerRecipeFiller(PanningRecipe.class,PanningRecipe.Type.INSTANCE,entry -> new ArrowProgressDisplay(PanningCategory.CATEGORY_ID,entry.value()));
    }

}
