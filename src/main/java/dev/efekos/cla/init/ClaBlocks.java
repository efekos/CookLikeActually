package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ClaBlocks {

    private static final AbstractBlock.Settings STAND_BLOCK_SETTINGS = AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).hardness(2.5f).resistance(3).mapColor(MapColor.PURPLE);

    public static final BlockSoundGroup PLATE_SOUNDS = new BlockSoundGroup(1f, 1f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GLASS_STEP, ClaSoundEvents.PLATE_PLACE, SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_FALL);
    public static final CuttingBoardBlock CUTTING_BOARD = register("cutting_board", copy(Blocks.OAK_PLANKS),CuttingBoardBlock::new);
    public static final PlateBlock PLATE = registerWithoutItem("plate", copy(Blocks.GLASS).sounds(PLATE_SOUNDS),PlateBlock::new);
    public static final PanBlock PAN = register("pan", copy(Blocks.GLASS).sounds(PLATE_SOUNDS),PanBlock::new);
    public static final PotBlock POT = register("pot", copy(Blocks.GLASS).sounds(PLATE_SOUNDS),PotBlock::new);
    public static final PlateRackBlock PLATE_RACK = register("plate_rack", copy(Blocks.DIRT).mapColor(MapColor.WHITE_GRAY).sounds(BlockSoundGroup.WOOD).nonOpaque(), PlateRackBlock::new);
    public static final ItemBoxBlock ITEM_BOX = register("item_box", copy(Blocks.OAK_PLANKS),ItemBoxBlock::new);
    public static final TrashCanBlock TRASH_CAN = register("trash_can", copy(Blocks.DIRT).mapColor(MapColor.GRAY).sounds(BlockSoundGroup.WOOD),TrashCanBlock::new);
    public static final CropBlock TOMATOES = register("tomatoes", copy(Blocks.CARROTS).mapColor(MapColor.DULL_RED),CropBlock::new);
    public static final LettuceBlock LETTUCES = register("lettuces", copy(Blocks.CARROTS).mapColor(MapColor.GREEN),LettuceBlock::new);

    public static final Block COOKING_STAND = register("cooking_stand", STAND_BLOCK_SETTINGS);
    public static final Block FRYING_STAND = register("frying_stand", STAND_BLOCK_SETTINGS.nonOpaque(),FryingStandBlock::new);
    public static final Block STAND = register("stand", STAND_BLOCK_SETTINGS);
    public static final Block WHITE_STAND = register("white_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.WHITE));
    public static final Block LIGHT_GRAY_STAND = register("light_gray_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_GRAY));
    public static final Block GRAY_STAND = register("gray_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.GRAY));
    public static final Block BLACK_STAND = register("black_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLACK));
    public static final Block BROWN_STAND = register("brown_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BROWN));
    public static final Block RED_STAND = register("red_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.RED));
    public static final Block ORANGE_STAND = register("orange_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.ORANGE));
    public static final Block YELLOW_STAND = register("yellow_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.YELLOW));
    public static final Block GREEN_STAND = register("green_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.GREEN));
    public static final Block LIME_STAND = register("lime_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIME));
    public static final Block CYAN_STAND = register("cyan_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.CYAN));
    public static final Block LIGHT_BLUE_STAND = register("light_blue_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_BLUE));
    public static final Block BLUE_STAND = register("blue_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLUE));
    public static final Block PURPLE_STAND = register("purple_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.PURPLE));
    public static final Block MAGENTA_STAND = register("magenta_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.MAGENTA));
    public static final Block PINK_STAND = register("pink_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.PINK));
    public static final Block WASHING_STAND = register("washing_stand", STAND_BLOCK_SETTINGS.nonOpaque(),WashingStandBlock::new);
    public static final Block WHITE_COOKING_STAND = register("white_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.WHITE));

    public static final Block LIGHT_GRAY_COOKING_STAND = register("light_gray_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_GRAY));
    public static final Block GRAY_COOKING_STAND = register("gray_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.GRAY));
    public static final Block BLACK_COOKING_STAND = register("black_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLACK));
    public static final Block BROWN_COOKING_STAND = register("brown_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BROWN));
    public static final Block RED_COOKING_STAND = register("red_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.RED));
    public static final Block ORANGE_COOKING_STAND = register("orange_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.ORANGE));
    public static final Block YELLOW_COOKING_STAND = register("yellow_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.YELLOW));
    public static final Block GREEN_COOKING_STAND = register("green_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.GREEN));
    public static final Block LIME_COOKING_STAND = register("lime_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIME));
    public static final Block CYAN_COOKING_STAND = register("cyan_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.CYAN));
    public static final Block LIGHT_BLUE_COOKING_STAND = register("light_blue_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_BLUE));
    public static final Block BLUE_COOKING_STAND = register("blue_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLUE));
    public static final Block PURPLE_COOKING_STAND = register("purple_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.PURPLE));
    public static final Block MAGENTA_COOKING_STAND = register("magenta_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.MAGENTA));
    public static final Block PINK_COOKING_STAND = register("pink_cooking_stand", STAND_BLOCK_SETTINGS.mapColor(DyeColor.PINK));

    public static void run() {

    }

    private static AbstractBlock.@NotNull Settings copy(Block b) {
        return AbstractBlock.Settings.copy(b);
    }

    private static <T extends Block> T register(String id, AbstractBlock.Settings settings, Function<AbstractBlock.Settings,T> blockCreator) {
        Identifier identifier = Identifier.of(Main.MOD_ID, id);
        T registered = Registry.register(Registries.BLOCK, identifier, blockCreator.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK,identifier))));
        Registry.register(Registries.ITEM, identifier, new BlockItem(registered, new Item.Settings().useBlockPrefixedTranslationKey()));
        return registered;
    }

    private static Block register(String id, AbstractBlock.Settings settings) {
        return register(id,settings,Block::new);
    }

    private static <T extends Block> T registerWithoutItem(String id, AbstractBlock.Settings settings, Function<AbstractBlock.Settings,T> blockCreator) {
        Identifier identifier = Identifier.of(Main.MOD_ID, id);
        return Registry.register(Registries.BLOCK, identifier, blockCreator.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK,identifier))));
    }

}
