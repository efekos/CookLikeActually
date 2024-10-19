package dev.efekos.cla.mixin;

import dev.efekos.cla.client.renderer.FryingStandBlockEntityRenderer;
import dev.efekos.cla.init.ClaBlocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class ItemColorsMixin {

    @Inject(method = "create",at = @At("TAIL"))
    private static void create(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir){
        ItemColors value = cir.getReturnValue();

        value.register(FryingStandBlockEntityRenderer::provideItemColor, ClaBlocks.FRYING_STAND);
    }

}
