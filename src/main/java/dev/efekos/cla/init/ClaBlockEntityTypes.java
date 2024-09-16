package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.block.entity.ItemBoxBlockEntity;
import dev.efekos.cla.block.entity.PanBlockEntity;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ClaBlockEntityTypes {

    private static <T extends BlockEntity> @Nullable BlockEntityType<T> register(String name, BlockEntityType.BlockEntityFactory<T> factory, Block block) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Main.MOD_ID, name), BlockEntityType.Builder.create(factory, block).build());
    }

    public static void run() {

    }

    public static final BlockEntityType<PlateBlockEntity> PLATE = register("plate", PlateBlockEntity::new, ClaBlocks.PLATE);
    public static final BlockEntityType<CuttingBoardBlockEntity> CUTTING_BOARD = register("cutting_board", CuttingBoardBlockEntity::new, ClaBlocks.CUTTING_BOARD);
    public static final BlockEntityType<PanBlockEntity> PAN = register("pan", PanBlockEntity::new, ClaBlocks.PAN);
    public static final BlockEntityType<ItemBoxBlockEntity> ITEM_BOX = register("item_box",ItemBoxBlockEntity::new, ClaBlocks.ITEM_BOX);


}