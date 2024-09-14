package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.CuttingBoardBlock;
import dev.efekos.cla.block.PlateBlock;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
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
    public static final Block COOKING_STAND = register("cooking_stand",new Block(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.PURPLE)));

    public static final BlockEntityType<PlateBlockEntity> PLATE_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("cla", "plate"), BlockEntityType.Builder.create(PlateBlockEntity::new, PLATE).build());
    public static final BlockEntityType<CuttingBoardBlockEntity> CUTTING_BOARD_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("cla", "cutting_board"), BlockEntityType.Builder.create(CuttingBoardBlockEntity::new, CUTTING_BOARD).build());

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
