package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.PanBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PanSyncS2C implements CustomPayload {

    public static final Id<PanSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "pan_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, PanSyncS2C> CODEC = CustomPayload.codecOf(PanSyncS2C::write, PanSyncS2C::new);

    private final ItemStack item;
    private final int ticks;
    private final BlockPos pos;
    private final int maxTicks;
    private final boolean shouldRenderProgressBar;

    public PanSyncS2C(ItemStack item, int ticks, BlockPos pos, int maxTicks, boolean shouldRenderProgressBar) {
        this.item = item;
        this.ticks = ticks;
        this.pos = pos;
        this.maxTicks = maxTicks;
        this.shouldRenderProgressBar = shouldRenderProgressBar;
    }

    public PanSyncS2C(RegistryByteBuf buf) {
        this.ticks = buf.readInt();
        boolean b = buf.readBoolean();
        this.item = b ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
        this.pos = buf.readBlockPos();
        this.maxTicks = buf.readInt();
        this.shouldRenderProgressBar = buf.readBoolean();
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(this.ticks);
        boolean b = !this.item.isEmpty();
        buf.writeBoolean(b);
        if (b) ItemStack.PACKET_CODEC.encode(buf, this.item);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.maxTicks);
        buf.writeBoolean(this.shouldRenderProgressBar);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    @Environment(EnvType.CLIENT)
    public void handle(ClientPlayNetworking.Context context) {
        BlockEntity entity = context.client().world.getBlockEntity(pos);
        if (!(entity instanceof PanBlockEntity pan)) return;
        pan.setItemWithoutReset(item);
        pan.setTicks(ticks);
        pan.setMaxTicks(maxTicks);
        pan.setShouldRenderProgressBar(shouldRenderProgressBar);
    }

}
