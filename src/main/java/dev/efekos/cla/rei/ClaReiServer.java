package dev.efekos.cla.rei;

import dev.efekos.cla.recipe.CuttingRecipe;
import dev.efekos.cla.recipe.FryingRecipe;
import dev.efekos.cla.recipe.PanningRecipe;
import dev.efekos.cla.recipe.PottingRecipe;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;

public class ClaReiServer implements REICommonPlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CuttingCategory.ID,CuttingDisplay.SERIALIZER);
        registry.register(FryingCategory.ID, ArrowProgressDisplay.Serializer.get(FryingCategory.CATEGORY_ID));
        registry.register(PanningCategory.ID,ArrowProgressDisplay.Serializer.get(PanningCategory.CATEGORY_ID));
        registry.register(PottingCategory.ID,ArrowProgressDisplay.Serializer.get(PottingCategory.CATEGORY_ID));
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {

        registry.beginRecipeFiller(CuttingRecipe.class)
                .filterType(CuttingRecipe.Type.INSTANCE)
                .fill(CuttingDisplay::new);

        registry.beginRecipeFiller(FryingRecipe.class)
                .filterType(FryingRecipe.Type.INSTANCE)
                .fill(entry -> new ArrowProgressDisplay(FryingCategory.CATEGORY_ID,entry.value()));

        registry.beginRecipeFiller(PottingRecipe.class)
                .filterType(PottingRecipe.Type.INSTANCE)
                .fill(entry -> new ArrowProgressDisplay(PottingCategory.CATEGORY_ID,entry.value()));

        registry.beginRecipeFiller(PanningRecipe.class)
                .filterType(PanningRecipe.Type.INSTANCE)
                .fill(entry -> new ArrowProgressDisplay(PanningCategory.CATEGORY_ID,entry.value()));

    }

}
