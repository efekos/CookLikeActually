package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ClaTags {

    public static final TagKey<Item> RENDER_AS_ITEM = item("render_as_item");
    public static final TagKey<Block> COOKING_STANDS = block("cooking_stands");

    private static TagKey<Item> item(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(Main.MOD_ID, id));
    }
    private static TagKey<Block> block(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Main.MOD_ID, id));
    }

}