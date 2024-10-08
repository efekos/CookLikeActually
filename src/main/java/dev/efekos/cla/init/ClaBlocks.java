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
