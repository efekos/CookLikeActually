package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.recipe.CuttingRecipe;
import dev.efekos.cla.recipe.FryingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClaRecipeTypes {

    public static void run(){
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(Main.MOD_ID,"cutting"), CuttingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER,Identifier.of(Main.MOD_ID,"cutting"), CuttingRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE,Identifier.of(Main.MOD_ID,"frying"), FryingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER,Identifier.of(Main.MOD_ID,"frying"), FryingRecipe.Serializer.INSTANCE);
    }

}
