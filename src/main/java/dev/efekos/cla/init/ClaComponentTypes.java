package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.function.UnaryOperator;

public class ClaComponentTypes {

    public static final ComponentType<ItemStack> ITEM = register("item", builder -> builder.codec(ItemStack.CODEC).packetCodec(PacketCodecs.codec(ItemStack.CODEC)));
    public static final ComponentType<Integer> CUTS = register("cuts", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<List<ItemStack>> ITEMS = register("items", builder -> builder.codec(ItemStack.CODEC.listOf()).packetCodec(ItemStack.LIST_PACKET_CODEC));
    public static final ComponentType<Identifier> COURSE_ID = register("course", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));
    public static final ComponentType<Integer> TICKS = register("ticks",builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return (ComponentType) Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Main.MOD_ID, id), ((ComponentType.Builder) builderOperator.apply(ComponentType.builder())).build());
    }

    public static void run() {

    }


}