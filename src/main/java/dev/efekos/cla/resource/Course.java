package dev.efekos.cla.resource;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Course(Identifier id, Identifier modelId, List<Ingredient> ingredients, int nutrition, int saturation, String translationKey, Optional<List<Ingredient>> trnsfrmrs) {

    public boolean matches(List<ItemStack> stacks) {
        for (Ingredient ingredient : ingredients) if (stacks.stream().noneMatch(ingredient)) return false;
        for (ItemStack stack : stacks)
            if (ingredients.stream().noneMatch(ingredient -> ingredient.test(stack))) return false;
        return true;
    }

    public List<Ingredient> transformers(){
        return trnsfrmrs.orElseGet(ArrayList::new);
    }

    public boolean canTransform(ItemStack stack) {
        return transformers().stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    public Course copyWithId(Identifier _id){
        return new Course(_id, modelId, ingredients, nutrition, saturation, translationKey, trnsfrmrs);
    }

    public Course copyWithTranslationKey(String key){
        return new Course(id, modelId, ingredients, nutrition, saturation, key, trnsfrmrs);
    }

}