package dev.efekos.cla.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;

public interface RecipeWithArrowProgress<T extends RecipeInput> extends Recipe<T> {
    int getTime();

    Ingredient getItem();

    ItemStack getRes();
}
