package dev.efekos.cla.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.efekos.cla.recipe.CuttingRecipe;
import dev.efekos.cla.recipe.RecipeWithArrowProgress;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArrowProgressDisplay extends BasicDisplay {

    private final int time;
    private final CategoryIdentifier<?> categoryIdentifier;

    public ArrowProgressDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int time, CategoryIdentifier<?> categoryIdentifier) {
        super(inputs, outputs);
        this.time = time;
        this.categoryIdentifier = categoryIdentifier;
    }

    public ArrowProgressDisplay(CategoryIdentifier<?> id, RecipeWithArrowProgress<?> entry) {
        this(List.of(EntryIngredient.of(EntryStacks.of(entry.getItem().getMatchingItems().getFirst().value()))), List.of(EntryIngredient.of(EntryStacks.of(entry.getRes()))), entry.getTime(), id);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return categoryIdentifier;
    }

    public int getTime() {
        return time;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null; //TODO
    }

}
