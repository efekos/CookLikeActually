package dev.efekos.cla.mixin;

import dev.efekos.cla.block.entity.SyncAbleBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Shadow
    @Nullable
    protected World world;

    @Shadow
    @Final
    protected BlockPos pos;

    @Inject(method = "markDirty()V", at = @At("HEAD"))
    public void markDirty(CallbackInfo ci) {
        if (this instanceof SyncAbleBlockEntity<?> s && !this.world.isClient)
            for (ServerPlayerEntity entity : PlayerLookup.tracking(((ServerWorld) world), pos))
                ServerPlayNetworking.send(entity, s.createSyncPacket());
    }

}
