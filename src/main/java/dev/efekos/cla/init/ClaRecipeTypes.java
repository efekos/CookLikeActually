package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.recipe.CuttingRecipe;
import dev.efekos.cla.recipe.FryingRecipe;
import dev.efekos.cla.recipe.PanningRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClaRecipeTypes {

    public static void run(){
        register("cutting",CuttingRecipe.Serializer.INSTANCE,CuttingRecipe.Type.INSTANCE);
        register("frying",FryingRecipe.Serializer.INSTANCE,FryingRecipe.Type.INSTANCE);
        register("panning",PanningRecipe.Serializer.INSTANCE,PanningRecipe.Type.INSTANCE);
    }

    private static <T extends Recipe<?>> void register(String id, RecipeSerializer<T> serializer, RecipeType<T> type) {
        Registry.register(Registries.RECIPE_TYPE,Identifier.of(Main.MOD_ID,id), type);
        Registry.register(Registries.RECIPE_SERIALIZER,Identifier.of(Main.MOD_ID,id), serializer);
    }

}
