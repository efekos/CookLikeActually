package dev.efekos.cla.client.renderer.bar;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public record ProgressBarTextures(Identifier empty, Identifier full) {
}