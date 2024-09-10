package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClaItems {

    public static void run(){

    }

    private static <T extends Item> T register(String id,T item){
        return Registry.register(Registries.ITEM, Identifier.of(Main.MOD_ID, id), item);
    }

}
