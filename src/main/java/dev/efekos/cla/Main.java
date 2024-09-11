package dev.efekos.cla;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaGroups;
import dev.efekos.cla.init.ClaItems;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static final String MOD_ID = "cla";

    @Override
    public void onInitialize() {
        ClaComponentTypes.run();
        ClaBlocks.run();
        ClaItems.run();
        ClaGroups.run();
    }

}