package dev.efekos.cla.mixin;

import dev.efekos.cla.init.ClaBlocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "create",at = @At("TAIL"), cancellable = true)
    private static void cr(CallbackInfoReturnable<BlockColors> cir){
        BlockColors blockColors = cir.getReturnValue();
        blockColors.registerColorProvider((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1, ClaBlocks.WASHING_STAND);
        cir.setReturnValue(blockColors);
    }

}
