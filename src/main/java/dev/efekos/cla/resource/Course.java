package dev.efekos.cla.resource;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.List;

public record Course(Identifier id, Identifier modelId, List<Ingredient> ingredients, int nutrition, int saturation,
                     float eatSeconds, String translationKey, List<Ingredient> transformers) {

    public boolean matches(List<ItemStack> stacks) {
        for (Ingredient ingredient : ingredients) if (stacks.stream().noneMatch(ingredient)) return false;
        for (ItemStack stack : stacks)
            if (ingredients.stream().noneMatch(ingredient -> ingredient.test(stack))) return false;
        return true;
    }

    public boolean canTransform(ItemStack stack) {
        return transformers.stream().anyMatch(ingredient -> ingredient.test(stack));
    }

}