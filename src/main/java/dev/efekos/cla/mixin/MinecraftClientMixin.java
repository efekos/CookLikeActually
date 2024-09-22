package dev.efekos.cla.mixin;

import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;
import dev.efekos.cla.util.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMinecraftClientMixin {

    @Unique
    private ProgressBarRenderer progressBarRenderer;

    @Unique
    public ProgressBarRenderer cla$getProgressBarRenderer() {
        return progressBarRenderer;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(RunArgs args, CallbackInfo ci) {
        this.progressBarRenderer = new ProgressBarRenderer();
    }

}
