package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClaSoundEvents {

    public static final SoundEvent KNIFE_SLICE = of("item.knife.slice");
    public static final SoundEvent PLATE_PLACE = of("block.plate.place");

    private static @NotNull SoundEvent of(String path) {
        return SoundEvent.of(Identifier.of(Main.MOD_ID, path));
    }

    public static void run() {

    }

}
