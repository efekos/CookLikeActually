package dev.efekos.cla.recipe;

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

public class CuttingRecipe implements Recipe<SingleStackRecipeInput> {

    private Ingredient item;
    private ItemStack result;
    private int cuts;

    public CuttingRecipe(Ingredient item, ItemStack result, int cuts) {
        this.item = item;
        this.result = result;
        this.cuts = cuts;
    }

    public Ingredient getItem() {
        return item;
    }

    public void setItem(Ingredient item) {
        this.item = item;
    }

    public ItemStack getRes() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public int getCuts() {
        return cuts;
    }

    public void setCuts(int cuts) {
        this.cuts = cuts;
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

    public static final class Serializer implements RecipeSerializer<CuttingRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<CuttingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item").forGetter(CuttingRecipe::getItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CuttingRecipe::getRes),
                Codecs.POSITIVE_INT.fieldOf("cuts").forGetter(CuttingRecipe::getCuts)
        ).apply(instance, CuttingRecipe::new));
        public final PacketCodec<RegistryByteBuf, CuttingRecipe> PACKET_CODEC = PacketCodec.ofStatic(this::write, this::read);

        @Override
        public MapCodec<CuttingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, CuttingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public void write(RegistryByteBuf buf, CuttingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getItem());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getRes());
            buf.writeInt(recipe.getCuts());
        }

        public CuttingRecipe read(RegistryByteBuf buf) {
            Ingredient item = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int cuts = buf.readInt();
            return new CuttingRecipe(item, result, cuts);
        }

    }

    public static final class Type implements RecipeType<CuttingRecipe> {

        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return "cla:cutting";
        }
    }

}