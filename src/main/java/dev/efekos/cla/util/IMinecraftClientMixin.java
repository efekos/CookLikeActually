package dev.efekos.cla.util;

import dev.efekos.cla.client.renderer.BillboardTextureRenderer;
import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;

public interface IMinecraftClientMixin {

    ProgressBarRenderer cla$getProgressBarRenderer();

    BillboardTextureRenderer cla$getBillboardTextureRenderer();

}
