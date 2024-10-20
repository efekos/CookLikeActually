package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.*;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ClaBlocks {

    public static final BlockSoundGroup PLATE_SOUNDS = new BlockSoundGroup(1f, 1f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_GLASS_STEP, ClaSoundEvents.PLATE_PLACE, SoundEvents.BLOCK_GLASS_HIT, SoundEvents.BLOCK_GLASS_FALL);
    public static final CuttingBoardBlock CUTTING_BOARD = register("cutting_board", new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final PlateBlock PLATE = registerWithoutItem("plate", new PlateBlock(AbstractBlock.Settings.copy(Blocks.GLASS).sounds(PLATE_SOUNDS)));
    public static final PanBlock PAN = register("pan", new PanBlock(AbstractBlock.Settings.copy(Blocks.GLASS).sounds(PLATE_SOUNDS)));
    public static final PotBlock POT = register("pot", new PotBlock(AbstractBlock.Settings.copy(Blocks.GLASS).sounds(PLATE_SOUNDS)));
    public static final PlateRackBlock PLATE_RACK = register("plate_rack", new PlateRackBlock(AbstractBlock.Settings.copy(Blocks.DIRT).mapColor(MapColor.WHITE_GRAY).sounds(BlockSoundGroup.WOOD).nonOpaque()));
    public static final ItemBoxBlock ITEM_BOX = register("item_box", new ItemBoxBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final TrashCanBlock TRASH_CAN = register("trash_can", new TrashCanBlock(AbstractBlock.Settings.copy(Blocks.DIRT).mapColor(MapColor.GRAY).sounds(BlockSoundGroup.WOOD)));
    public static final CropBlock TOMATOES = register("tomatoes", new TomatoesBlock(AbstractBlock.Settings.copy(Blocks.CARROTS).mapColor(MapColor.DULL_RED)));
    public static final LettuceBlock LETTUCES = register("lettuces", new LettuceBlock(AbstractBlock.Settings.copy(Blocks.CARROTS).mapColor(MapColor.GREEN)));
    private static final AbstractBlock.Settings STAND_BLOCK_SETTINGS = AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).hardness(2.5f).resistance(3).mapColor(MapColor.PURPLE);
    public static final Block COOKING_STAND = register("cooking_stand", new Block(STAND_BLOCK_SETTINGS));
    public static final Block FRYING_STAND = register("frying_stand", new FryingStandBlock(STAND_BLOCK_SETTINGS.nonOpaque()));
    public static final Block STAND = register("stand", new Block(STAND_BLOCK_SETTINGS));
    public static final Block WHITE_STAND = register("white_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.WHITE)));
    public static final Block LIGHT_GRAY_STAND = register("light_gray_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_GRAY)));
    public static final Block GRAY_STAND = register("gray_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.GRAY)));
    public static final Block BLACK_STAND = register("black_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLACK)));
    public static final Block BROWN_STAND = register("brown_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.BROWN)));
    public static final Block RED_STAND = register("red_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.RED)));
    public static final Block ORANGE_STAND = register("orange_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.ORANGE)));
    public static final Block YELLOW_STAND = register("yellow_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.YELLOW)));
    public static final Block GREEN_STAND = register("green_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.GREEN)));
    public static final Block LIME_STAND = register("lime_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIME)));
    public static final Block CYAN_STAND = register("cyan_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.CYAN)));
    public static final Block LIGHT_BLUE_STAND = register("light_blue_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.LIGHT_BLUE)));
    public static final Block BLUE_STAND = register("light_blue_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.BLUE)));
    public static final Block PURPLE_STAND = register("purple_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.PURPLE)));
    public static final Block MAGENTA_STAND = register("magenta_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.MAGENTA)));
    public static final Block PINK_STAND = register("pink_stand", new Block(STAND_BLOCK_SETTINGS.mapColor(DyeColor.PINK)));
    public static final Block WASHING_STAND = register("washing_stand", new WashingStandBlock(STAND_BLOCK_SETTINGS.nonOpaque()));

    public static void run() {

    }

    private static <T extends Block> T register(String id, T block) {
        T registered = Registry.register(Registries.BLOCK, Identifier.of(Main.MOD_ID, id), block);
        Registry.register(Registries.ITEM, Identifier.of(Main.MOD_ID, id), new BlockItem(registered, new Item.Settings()));
        return registered;
    }


    private static <T extends Block> T registerWithoutItem(String id, T block) {
        return Registry.register(Registries.BLOCK, Identifier.of(Main.MOD_ID, id), block);
    }

}
