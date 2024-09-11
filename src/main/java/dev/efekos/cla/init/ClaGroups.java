package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class ClaGroups {

    public static void run(){
        Registry.register(Registries.ITEM_GROUP, Identifier.of(Main.MOD_ID,"cla"),new ItemGroup.Builder(ItemGroup.Row.TOP,3)
                .displayName(Text.translatable("itemGroup.cla")).icon(ClaItems.TOMATO::getDefaultStack)
                .entries((displayContext, entries) -> {
                    for (ItemConvertible item : List.of(
                            ClaItems.TOMATO, ClaItems.CUT_TOMATO, ClaItems.LETTUCE, ClaItems.CUT_LETTUCE, ClaItems.PATTY, ClaItems.COOKED_PATTY, ClaItems.CUT_POTATO, ClaItems.FRIES,
                            ClaItems.PLATE, ClaItems.KNIFE, ClaBlocks.CUTTING_BOARD
                    ))
                        entries.add(item);
                }).build());
    }

}
