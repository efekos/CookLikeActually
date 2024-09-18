package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.item.KnifeItem;
import dev.efekos.cla.item.PlateItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Optional;

public class ClaItems {

    public static final Item TOMATO = register("tomato", new Item(new Item.Settings().rarity(Rarity.COMMON).food(new FoodComponent(2, 4, false, 3, Optional.empty(), List.of()))));
    public static final Item LETTUCE = register("lettuce", new Item(new Item.Settings().rarity(Rarity.COMMON).food(new FoodComponent(4, 2, false, 2.5f, Optional.empty(), List.of()))));
    public static final Item CUT_TOMATO = register("cut_tomato", new Item(new Item.Settings().rarity(Rarity.COMMON).food(new FoodComponent(2, 4, false, 1f, Optional.empty(), List.of()))));
    public static final Item CUT_LETTUCE = register("cut_lettuce", new Item(new Item.Settings().rarity(Rarity.COMMON).food(new FoodComponent(4, 2, false, 0.8f, Optional.empty(), List.of()))));
    public static final Item PLATE = register("plate", new PlateItem(new Item.Settings().rarity(Rarity.COMMON)));
    public static final Item PATTY = register("patty", new Item(new Item.Settings().rarity(Rarity.COMMON).food(FoodComponents.BEEF)));
    public static final Item COOKED_PATTY = register("cooked_patty", new Item(new Item.Settings().rarity(Rarity.COMMON).food(FoodComponents.COOKED_BEEF)));
    public static final Item BURNED_PATTY = register("burned_patty", new Item(new Item.Settings().rarity(Rarity.COMMON)));
    public static final Item CUT_POTATO = register("cut_potato", new Item(new Item.Settings().rarity(Rarity.COMMON).food(FoodComponents.POTATO)));
    public static final Item FRIES = register("fries", new Item(new Item.Settings().rarity(Rarity.COMMON).food(FoodComponents.BAKED_POTATO)));
    public static final Item BURNED_FRIES = register("burned_fries",new Item(new Item.Settings().rarity(Rarity.COMMON)));
    public static final Item BUNS = register("buns", new Item(new Item.Settings().rarity(Rarity.COMMON)));
    public static final KnifeItem KNIFE = register("knife", new KnifeItem(ToolMaterials.IRON, new Item.Settings().rarity(Rarity.COMMON).maxCount(1).attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.IRON, 1, -0.8F))));
    public static final Item FRYING_SIEVE = register("frying_sieve", new Item(new Item.Settings().rarity(Rarity.COMMON).maxCount(1)));
    public static final Item CHEESE = register("cheese",new Item(new Item.Settings().rarity(Rarity.COMMON)));
    public static final Item CUT_CHEESE = register("cut_cheese",new Item(new Item.Settings().rarity(Rarity.COMMON)));

    public static void run() {

    }

    private static <T extends Item> T register(String id, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(Main.MOD_ID, id), item);
    }

}
