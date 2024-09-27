package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
                            ClaItems.CUT_POTATO, ClaItems.FRIES, ClaItems.BURNED_FRIES, ClaItems.CHEESE, ClaItems.CUT_CHEESE, ClaItems.BUNS, ClaItems.FLOUR, ClaItems.FLOUR_PACK, ClaItems.TORTILLA,
                            ClaItems.CUBIC_BEEF, ClaItems.CUBIC_COOKED_BEEF, ClaItems.CUBIC_BURNED_BEEF, ClaItems.CUBIC_CHICKEN, ClaItems.CUBIC_COOKED_CHICKEN, ClaItems.CUBIC_BURNED_CHICKEN,
                            ClaItems.RICE,ClaItems.COOKED_RICE,
                            ClaItems.PLATE, ClaItems.DIRTY_PLATE, ClaItems.KNIFE, ClaBlocks.CUTTING_BOARD, ClaBlocks.PAN, ClaBlocks.POT, ClaItems.FRYING_SIEVE,
                            ClaBlocks.COOKING_STAND, ClaBlocks.FRYING_STAND, ClaBlocks.STAND, ClaBlocks.WASHING_STAND, ClaBlocks.PLATE_RACK, ClaBlocks.ITEM_BOX, ClaBlocks.TRASH_CAN,
                            ClaItems.ORDER_NOTE
                    ))
                        entries.add(item);
                }).build());

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if (RegistryKey.of(RegistryKeys.ITEM_GROUP, Registries.ITEM_GROUP.getId(group)).equals(ItemGroups.FOOD_AND_DRINK)) {
                entries.addAfter(Items.COOKED_BEEF, ClaItems.BURNED_BEEF);
                entries.addAfter(Items.COOKED_CHICKEN, ClaItems.BURNED_CHICKEN);
                entries.addAfter(Items.COOKED_COD, ClaItems.BURNED_COD);
                entries.addAfter(Items.COOKED_SALMON, ClaItems.BURNED_SALMON);
                entries.addAfter(Items.COOKED_RABBIT, ClaItems.BURNED_RABBIT);
                entries.addAfter(Items.COOKED_MUTTON, ClaItems.BURNED_MUTTON);
                entries.addAfter(Items.COOKED_PORKCHOP, ClaItems.BURNED_PORKCHOP);
            }
        });
    }

}
