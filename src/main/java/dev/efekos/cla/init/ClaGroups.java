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

    public static void run() {
        Registry.register(Registries.ITEM_GROUP, Identifier.of(Main.MOD_ID, "cla"), new ItemGroup.Builder(ItemGroup.Row.TOP, 3)
                .displayName(Text.translatable("itemGroup.cla")).icon(ClaItems.TOMATO::getDefaultStack)
                .entries((displayContext, entries) -> {
                    for (ItemConvertible item : List.of(
                            ClaItems.TOMATO_SEEDS, ClaItems.TOMATO, ClaItems.CUT_TOMATO, ClaItems.LETTUCE, ClaItems.CUT_LETTUCE, ClaItems.PATTY, ClaItems.COOKED_PATTY, ClaItems.BURNED_PATTY,
                            ClaItems.CUT_POTATO, ClaItems.FRIES, ClaItems.BURNED_FRIES, ClaItems.CHEESE, ClaItems.CUT_CHEESE, ClaItems.BUNS,
                            ClaItems.PLATE, ClaItems.KNIFE, ClaBlocks.CUTTING_BOARD, ClaBlocks.PAN, ClaItems.FRYING_SIEVE,
                            ClaBlocks.COOKING_STAND, ClaBlocks.FRYING_STAND, ClaBlocks.STAND, ClaBlocks.WASHING_STAND, ClaBlocks.PLATE_RACK, ClaBlocks.ITEM_BOX, ClaBlocks.TRASH_CAN,
                            ClaItems.ORDER_NOTE
                    ))
                        entries.add(item);
                }).build());
    }

}
