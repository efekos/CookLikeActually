package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.item.KnifeItem;
import dev.efekos.cla.item.OilBottleItem;
import dev.efekos.cla.item.OrderNoteItem;
import dev.efekos.cla.item.PlateItem;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class ClaItems {

    public static final Item TOMATO = register("tomato", settings -> new Item(settings.rarity(Rarity.COMMON).food(new FoodComponent(2, 4, false))));
    public static final Item LETTUCE = register("lettuce",settings ->  new BlockItem(ClaBlocks.LETTUCES, settings.useItemPrefixedTranslationKey().rarity(Rarity.COMMON).food(new FoodComponent(4, 2, false))));
    public static final Item CUT_TOMATO = register("cut_tomato", settings -> new Item(settings.rarity(Rarity.COMMON).food(new FoodComponent(2, 4, false))));
    public static final Item CUT_LETTUCE = register("cut_lettuce", settings -> new Item(settings.rarity(Rarity.COMMON).food(new FoodComponent(4, 2, false))));
    public static final Item PLATE = register("plate", settings -> new PlateItem(settings.rarity(Rarity.COMMON)));
    public static final Item PATTY = register("patty", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.BEEF)));
    public static final Item COOKED_PATTY = register("cooked_patty", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.COOKED_BEEF)));
    public static final Item BURNED_PATTY = register("burned_patty", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item CUT_POTATO = register("cut_potato", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.POTATO)));
    public static final Item FRIES = register("fries", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.BAKED_POTATO)));
    public static final Item BURNED_FRIES = register("burned_fries", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item BUNS = register("buns", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final KnifeItem KNIFE = register("knife", settings -> new KnifeItem(ToolMaterial.IRON, ToolMaterial.IRON.applySwordSettings(settings.rarity(Rarity.COMMON).maxCount(1),1,-0.8F)));
    public static final Item FRYING_SIEVE = register("frying_sieve", settings -> new Item(settings.rarity(Rarity.COMMON).maxCount(1)));
    public static final Item CHEESE = register("cheese", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item CUT_CHEESE = register("cut_cheese", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item ORDER_NOTE = register("order_note", settings -> new OrderNoteItem(settings.rarity(Rarity.COMMON).maxCount(1).component(ClaComponentTypes.COURSE_ID, Identifier.of("cla", "salad"))));
    public static final Item TOMATO_SEEDS = register("tomato_seeds", settings -> new BlockItem(ClaBlocks.TOMATOES, settings.useItemPrefixedTranslationKey().rarity(Rarity.COMMON)));
    public static final Item DIRTY_PLATE = register("dirty_plate", settings -> new Item(settings.rarity(Rarity.COMMON).maxCount(1)));
    public static final Item FLOUR = register("flour", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item FLOUR_PACK = register("flour_pack", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item TORTILLA = register("tortilla", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final Item CUBIC_BEEF = register("cubic_beef", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.BEEF)));
    public static final Item CUBIC_CHICKEN = register("cubic_chicken", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.CHICKEN)));
    public static final Item CUBIC_COOKED_BEEF = register("cubic_cooked_beef", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.COOKED_BEEF)));
    public static final Item CUBIC_COOKED_CHICKEN = register("cubic_cooked_chicken", settings -> new Item(settings.rarity(Rarity.COMMON).food(FoodComponents.COOKED_CHICKEN)));
    public static final Item CUBIC_BURNED_BEEF = register("cubic_burned_beef", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_BEEF),poison())));
    public static final Item CUBIC_BURNED_CHICKEN = register("cubic_burned_chicken", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_CHICKEN),poison())));
    public static final Item RICE = register("rice", settings -> new Item(settings.rarity(Rarity.COMMON).food(new FoodComponent(2, 3, false))));
    public static final Item COOKED_RICE = register("cooked_rice", settings -> new Item(settings.rarity(Rarity.COMMON).food(rice())));
    public static final Item BURNED_RICE = register("burned_rice", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(rice()),poison())));
    public static final Item EMPTY_OIL_BOTTLE = register("empty_oil_bottle", settings -> new Item(settings.rarity(Rarity.COMMON)));
    public static final OilBottleItem OIL_BOTTLE = register("oil_bottle", settings -> new OilBottleItem(settings.rarity(Rarity.COMMON).maxCount(1)));
    public static final Item BURNED_BEEF = register("burned_beef", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_BEEF),poison())));
    public static final Item BURNED_CHICKEN = register("burned_chicken", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_CHICKEN),poison())));
    public static final Item BURNED_MUTTON = register("burned_mutton", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_MUTTON),poison())));
    public static final Item BURNED_SALMON = register("burned_salmon", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_SALMON),poison())));
    public static final Item BURNED_COD = register("burned_cod", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_COD),poison())));
    public static final Item BURNED_RABBIT = register("burned_rabbit", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_RABBIT),poison())));
    public static final Item BURNED_PORKCHOP = register("burned_porkchop", settings -> new Item(settings.rarity(Rarity.COMMON).food(burn(FoodComponents.COOKED_PORKCHOP),poison())));

    private static @NotNull FoodComponent rice() {
        return new FoodComponent(2, 6, false);
    }

    private static FoodComponent burn(FoodComponent component) {
        return new FoodComponent(0, Math.max(0.25f, component.saturation() / 4f), component.canAlwaysEat());
    }

    private static ConsumableComponent poison(){
        return ConsumableComponents.food().consumeEffect(new ApplyEffectsConsumeEffect(List.of(new StatusEffectInstance(StatusEffects.POISON, 200, 2)))).build();
    }

    public static void run() {

    }

    private static <T extends Item> T register(String id, Function<Item.Settings,T> settingsToItem) {
        Identifier identifier = Identifier.of(Main.MOD_ID, id);
        return Registry.register(Registries.ITEM, identifier, settingsToItem.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM,identifier))));
    }

}
