package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClaBlocks {
    
    public static void run(){

    }

    private static <T extends Block> T register(String id, T item){
        return Registry.register(Registries.BLOCK, Identifier.of(Main.MOD_ID, id), item);
    }

}
