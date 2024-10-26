package dev.efekos.cla.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public class PottingRecipe implements RecipeWithArrowProgress<SingleStackRecipeInput> {

    private final Ingredient item;
    private final ItemStack result;
    private final int time;
    private final boolean burnRecipe;

    public PottingRecipe(Ingredient item, ItemStack result, int time) {
        this.item = item;
        this.result = result;
        this.time = time;
        this.burnRecipe = true;
    }

    public PottingRecipe(Ingredient item, ItemStack result, int time, boolean burnRecipe) {
        this.item = item;
        this.result = result;
        this.time = time;
        this.burnRecipe = burnRecipe;
    }

    public boolean hasProgressBar() {
        return !burnRecipe;
    }

    public boolean isBurnRecipe() {
        return burnRecipe;
    }

    public SimpleParticleType getParticleType() {
        return burnRecipe ? ParticleTypes.DUST_PLUME : ParticleTypes.FLAME;
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

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return item.test(input.item());
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleStackRecipeInput>> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<SingleStackRecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.NONE;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public static final class Serializer implements RecipeSerializer<PottingRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<PottingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("item").forGetter(PottingRecipe::getItem),
                ItemStack.CODEC.fieldOf("result").forGetter(PottingRecipe::getRes),
                Codecs.POSITIVE_INT.fieldOf("time").forGetter(PottingRecipe::getTime),
                Codec.BOOL.optionalFieldOf("burn", false).forGetter(PottingRecipe::isBurnRecipe)
        ).apply(instance, PottingRecipe::new));
        public final PacketCodec<RegistryByteBuf, PottingRecipe> PACKET_CODEC = PacketCodec.ofStatic(this::write, this::read);

        @Override
        public MapCodec<PottingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, PottingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public void write(RegistryByteBuf buf, PottingRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.getItem());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getRes());
            buf.writeInt(recipe.getTime());
            buf.writeBoolean(recipe.isBurnRecipe());
        }

        public PottingRecipe read(RegistryByteBuf buf) {
            Ingredient item = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int cuts = buf.readInt();
            boolean hasProgressBar = buf.readBoolean();
            return new PottingRecipe(item, result, cuts, hasProgressBar);
        }

    }

    public static final class Type implements RecipeType<PottingRecipe> {

        public static final Type INSTANCE = new Type();

        @Override
        public String toString() {
            return "cla:potting";
        }
    }

}