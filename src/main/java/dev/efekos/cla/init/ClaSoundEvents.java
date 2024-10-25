package dev.efekos.cla.init;

import dev.efekos.cla.Main;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ClaSoundEvents {

    public static final SoundEvent KNIFE_SLICE = of("item.knife.slice");
    public static final SoundEvent PLATE_PLACE = of("block.plate.place");
    public static final SoundEvent PAN_COOKING = of("block.pan.cook");
    public static final SoundEvent PLATE_PICKUP = of("block.plate.pickup");
    public static final SoundEvent PLATE_PLACE_TO_RACK = of("block.plate_rack.place_plate");
    public static final SoundEvent TRASH_CAN_PUT_TRASH = of("block.trash_can.put_trash");

    private static @NotNull SoundEvent of(String path) {
        return SoundEvent.of(Identifier.of(Main.MOD_ID, path));
    }

    public static void run() {

    }

}
