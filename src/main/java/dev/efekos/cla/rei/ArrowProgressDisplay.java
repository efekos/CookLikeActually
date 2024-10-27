package dev.efekos.cla.rei;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.efekos.cla.recipe.RecipeWithArrowProgress;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        return Serializer.get(categoryIdentifier);
    }

    public static class Serializer implements DisplaySerializer<ArrowProgressDisplay> {


        private static final Map<CategoryIdentifier<?>, Serializer> instances = new HashMap<>();
        private final CategoryIdentifier<?> categoryIdentifier;
        private final MapCodec<ArrowProgressDisplay> codec;
        private final PacketCodec<RegistryByteBuf, ArrowProgressDisplay> packetCodec;

        private Serializer(CategoryIdentifier<?> categoryIdentifier) {
            this.categoryIdentifier = categoryIdentifier;

            this.codec = RecordCodecBuilder.mapCodec(i -> i.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ArrowProgressDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ArrowProgressDisplay::getOutputEntries),
                    Codecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ArrowProgressDisplay::getTime)
            ).apply(i, (entryIngredients, entryIngredients2, integer) -> new ArrowProgressDisplay(entryIngredients, entryIngredients2, integer, this.categoryIdentifier)));

            this.packetCodec = PacketCodecs.registryCodec(RecordCodecBuilder.create(i -> i.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(ArrowProgressDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(ArrowProgressDisplay::getOutputEntries),
                    Codecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ArrowProgressDisplay::getTime)
            ).apply(i, (ei, ei2, i2) -> new ArrowProgressDisplay(ei, ei2, i2, this.categoryIdentifier))));

        }

        public static Serializer get(CategoryIdentifier<?> id) {
            if (instances.containsKey(id)) return instances.get(id);
            System.out.println("created new instance of serializer for " + id);
            Serializer value = new Serializer(id);
            instances.put(id, value);
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Serializer that = (Serializer) o;
            return Objects.equals(categoryIdentifier, that.categoryIdentifier) && Objects.equals(codec, that.codec) && Objects.equals(packetCodec, that.packetCodec);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryIdentifier, codec, packetCodec);
        }

        @Override
        public MapCodec<ArrowProgressDisplay> codec() {
            return codec;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ArrowProgressDisplay> streamCodec() {
            return packetCodec;
        }

    }

}
