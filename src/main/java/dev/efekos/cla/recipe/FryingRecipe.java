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

public class FryingRecipe implements Recipe<SingleStackRecipeInput> {

    private Ingredient item;
    private ItemStack result;
    private int time;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public FryingRecipe(Ingredient item, ItemStack result, int time) {
        this.item = item;
        this.result = result;
        this.time = time;
    }

    public static final class Serializer implements RecipeSerializer<FryingRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<FryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item").forGetter(FryingRecipe::getItem),
                ItemStack.CODEC.fieldOf("result").forGetter(FryingRecipe::getRes),
                Codecs.POSITIVE_INT.fieldOf("time").forGetter(FryingRecipe::getTime)
        ).apply(instance, FryingRecipe::new));

        @Override
        public MapCodec<FryingRecipe> codec() {
            return CODEC;
        }

        public final PacketCodec<RegistryByteBuf, FryingRecipe> PACKET_CODEC = PacketCodec.ofStatic(this::write,this::read);

        @Override
        public PacketCodec<RegistryByteBuf, FryingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public void write(RegistryByteBuf buf, FryingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf,recipe.getItem());
            ItemStack.PACKET_CODEC.encode(buf,recipe.getRes());
            buf.writeInt(recipe.getTime());
        }

        public FryingRecipe read(RegistryByteBuf buf) {
            Ingredient item = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int cuts = buf.readInt();
            return new FryingRecipe(item,result,cuts);
        }

    }

    public static final class Type implements RecipeType<FryingRecipe> {

        public static final Type INSTANCE = new Type();

    }

}