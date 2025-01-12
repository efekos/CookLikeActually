package dev.efekos.cla.rei;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.efekos.cla.recipe.CuttingRecipe;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CuttingDisplay extends BasicDisplay {

    private int cuts;

    public CuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int cuts) {
        super(inputs, outputs);
        this.cuts = cuts;
    }

    public CuttingDisplay(RecipeEntry<CuttingRecipe> entry) {
        this(List.of(
                        EntryIngredient.of(entry.value().getItem().getMatchingItems().stream().map(registryEntry -> EntryStacks.of(registryEntry.value(),1)).toList())),
                List.of(EntryIngredient.of(EntryStacks.of(entry.value().getRes()))), entry.value().getCuts());
    }

    public CuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }


    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CuttingCategory.CATEGORY_ID;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return super.getInputEntries();
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return super.getOutputEntries();
    }

    public int getCuts() {
        return cuts;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public static final Serializer SERIALIZER = new Serializer();

    public static class Serializer implements DisplaySerializer<CuttingDisplay> {

        public static final MapCodec<CuttingDisplay> CODEC = RecordCodecBuilder.mapCodec(i->i.group(
                EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(CuttingDisplay::getInputEntries),
                EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(CuttingDisplay::getOutputEntries),
                Codecs.NON_NEGATIVE_INT.fieldOf("cuts").forGetter(CuttingDisplay::getCuts)
        ).apply(i,CuttingDisplay::new));

        public static final PacketCodec<RegistryByteBuf,CuttingDisplay> PACKET_CODEC = PacketCodecs.registryCodec(
        RecordCodecBuilder.create(i->i.group(
                EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(CuttingDisplay::getInputEntries),
                EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(CuttingDisplay::getOutputEntries),
                Codecs.NON_NEGATIVE_INT.fieldOf("cuts").forGetter(CuttingDisplay::getCuts)
        ).apply(i,CuttingDisplay::new)));

        @Override
        public MapCodec<CuttingDisplay> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CuttingDisplay> streamCodec() {
            return PACKET_CODEC;
        }

    }

}
