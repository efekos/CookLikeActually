package dev.efekos.cla.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class FryingRecipe implements Recipe<SingleStackRecipeInput> {

    private final Ingredient item;
    private final ItemStack result;
    private final int time;
    private final boolean progressBar;

    public FryingRecipe(Ingredient item, ItemStack result, int time, boolean progressBar) {
        this.item = item;
        this.result = result;
        this.time = time;
        this.progressBar = progressBar;
    }

    public Ingredient getItem() {
        return item;
    }

    public ItemStack getRes() {
        return result;
    }

    public int getTime() {
        return time;
    }

    public boolean hasProgressBar(){
        return progressBar;
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return item.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static final class Serializer implements RecipeSerializer<FryingRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<FryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item").forGetter(FryingRecipe::getItem),
                ItemStack.CODEC.fieldOf("result").forGetter(FryingRecipe::getRes),
                Codecs.POSITIVE_INT.fieldOf("time").forGetter(FryingRecipe::getTime),
                Codec.BOOL.optionalFieldOf("progress_bar",true).forGetter(FryingRecipe::hasProgressBar)
        ).apply(instance, FryingRecipe::new));
        public final PacketCodec<RegistryByteBuf, FryingRecipe> PACKET_CODEC = PacketCodec.ofStatic(this::write, this::read);

        @Override
        public MapCodec<FryingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FryingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public void write(RegistryByteBuf buf, FryingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getItem());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getRes());
            buf.writeInt(recipe.getTime());
            buf.writeBoolean(recipe.progressBar);
        }

        public FryingRecipe read(RegistryByteBuf buf) {
            Ingredient item = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int cuts = buf.readInt();
            boolean progressBar = buf.readBoolean();
            return new FryingRecipe(item, result, cuts, progressBar);
        }

    }

    public static final class Type implements RecipeType<FryingRecipe> {

        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return "cla:frying";
        }
    }

}