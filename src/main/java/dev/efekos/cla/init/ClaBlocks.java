package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.CuttingBoardBlock;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClaBlocks {


    public static final CuttingBoardBlock CUTTING_BOARD = register("cutting_board", new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));

    public static void run() {

    }    public static final BlockEntityType<CuttingBoardBlockEntity> CUTTING_BOARD_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("cla", "cutting_board"), BlockEntityType.Builder.create(CuttingBoardBlockEntity::new, CUTTING_BOARD).build());

    private static <T extends Block> T register(String id, T block) {
        T registered = Registry.register(Registries.BLOCK, Identifier.of(Main.MOD_ID, id), block);
        Registry.register(Registries.ITEM, Identifier.of(Main.MOD_ID, id), new BlockItem(registered, new Item.Settings()));
        return registered;
    }



}
