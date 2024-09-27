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

public class PotSyncS2C implements CustomPayload {

    public static final Id<PotSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "pot_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, PotSyncS2C> CODEC = CustomPayload.codecOf(PotSyncS2C::write, PotSyncS2C::new);

    private final ItemStack item;
    private final int ticks;
    private final BlockPos pos;

    public PotSyncS2C(ItemStack item, int ticks, BlockPos pos) {
        this.item = item;
        this.ticks = ticks;
        this.pos = pos;
    }

    public PotSyncS2C(RegistryByteBuf buf) {
        this.ticks = buf.readInt();
        boolean b = buf.readBoolean();
        this.item = b ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
        this.pos = buf.readBlockPos();
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(this.ticks);
        boolean b = !this.item.isEmpty();
        buf.writeBoolean(b);
        if (b) ItemStack.PACKET_CODEC.encode(buf, this.item);
        buf.writeBlockPos(this.pos);
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
    }

}
