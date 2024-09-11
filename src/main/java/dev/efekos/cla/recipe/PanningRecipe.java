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

public class PanningRecipe implements Recipe<SingleStackRecipeInput> {

    private final Ingredient item;
    private final ItemStack result;
    private final int time;

    public Ingredient getItem() {
        return item;
    }

    public ItemStack getRes() {
        return result;
    }

    public int getTime() {
        return time;
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

    public PanningRecipe(Ingredient item, ItemStack result, int time) {
        this.item = item;
        this.result = result;
        this.time = time;
    }

    public static final class Serializer implements RecipeSerializer<PanningRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<PanningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item").forGetter(PanningRecipe::getItem),
                ItemStack.CODEC.fieldOf("result").forGetter(PanningRecipe::getRes),
                Codecs.POSITIVE_INT.fieldOf("time").forGetter(PanningRecipe::getTime)
        ).apply(instance, PanningRecipe::new));

        @Override
        public MapCodec<PanningRecipe> codec() {
            return CODEC;
        }

        public final PacketCodec<RegistryByteBuf, PanningRecipe> PACKET_CODEC = PacketCodec.ofStatic(this::write,this::read);

        @Override
        public PacketCodec<RegistryByteBuf, PanningRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public void write(RegistryByteBuf buf, PanningRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf,recipe.getItem());
            ItemStack.PACKET_CODEC.encode(buf,recipe.getRes());
            buf.writeInt(recipe.getTime());
        }

        public PanningRecipe read(RegistryByteBuf buf) {
            Ingredient item = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int cuts = buf.readInt();
            return new PanningRecipe(item,result,cuts);
        }

    }

    public static final class Type implements RecipeType<PanningRecipe> {

        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return "cla:panning";
        }
    }

}