package dev.efekos.cla.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public interface IDrawContextMixin {

    void cla$drawItemWithScale(ItemStack item, int x, int y, float scale);

}
