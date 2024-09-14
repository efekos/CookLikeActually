package dev.efekos.cla.resource;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.List;

public record Course(String model, List<Ingredient> ingredients,int nutrition,int saturation,float eatSeconds,String translationKey) {

    public boolean matches(List<ItemStack> stacks){
        for (Ingredient ingredient : ingredients) if(stacks.stream().noneMatch(ingredient))return false;
        return true;
    }

}