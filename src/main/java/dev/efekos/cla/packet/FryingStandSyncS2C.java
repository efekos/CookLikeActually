package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.FryingStandBlockEntity;
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

public class FryingStandSyncS2C implements CustomPayload {

    public static final Id<FryingStandSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "frying_stand_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, FryingStandSyncS2C> CODEC = CustomPayload.codecOf(FryingStandSyncS2C::write, FryingStandSyncS2C::new);

    private final ItemStack item;
    private final int ticks;
    private final BlockPos pos;
    private final int oilCleanness;
    private final int maxTicks;

    public FryingStandSyncS2C(ItemStack item, int ticks, int oilCleanness, BlockPos pos,int maxTicks) {
        this.item = item;
        this.ticks = ticks;
        this.pos = pos;
        this.oilCleanness = oilCleanness;
        this.maxTicks = maxTicks;
    }

    public FryingStandSyncS2C(RegistryByteBuf buf) {
        this.ticks = buf.readInt();
        boolean b = buf.readBoolean();
        this.item = b ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
        this.pos = buf.readBlockPos();
        this.oilCleanness = buf.readInt();
        this.maxTicks = buf.readInt();
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(this.ticks);
        boolean b = !this.item.isEmpty();
        buf.writeBoolean(b);
        if (b) ItemStack.PACKET_CODEC.encode(buf, this.item);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.oilCleanness);
        buf.writeInt(this.maxTicks);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    @Environment(EnvType.CLIENT)
    public void handle(ClientPlayNetworking.Context context) {
        BlockEntity entity = context.client().world.getBlockEntity(pos);
        if (!(entity instanceof FryingStandBlockEntity pan)) return;
        pan.setItemWithoutReset(item);
        pan.setTicks(ticks);
        pan.setOilCleanness(oilCleanness);
        pan.setMaxTicks(maxTicks);
    }

}
