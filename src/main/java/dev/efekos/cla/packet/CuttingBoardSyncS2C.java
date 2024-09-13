package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
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

public class CuttingBoardSyncS2C implements CustomPayload {

    public static final CustomPayload.Id<CuttingBoardSyncS2C> PAYLOAD_ID = new CustomPayload.Id<>(Identifier.of(Main.MOD_ID, "block_entity_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, CuttingBoardSyncS2C> CODEC = CustomPayload.codecOf(CuttingBoardSyncS2C::write, CuttingBoardSyncS2C::new);

    private final ItemStack item;
    private final int cuts;
    private final BlockPos pos;

    public CuttingBoardSyncS2C(ItemStack item, int cuts, BlockPos pos) {
        this.item = item;
        this.cuts = cuts;
        this.pos = pos;
    }

    public CuttingBoardSyncS2C(RegistryByteBuf buf) {
        this.cuts = buf.readInt();
        boolean b = buf.readBoolean();
        this.item = b ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
        this.pos = buf.readBlockPos();
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(this.cuts);
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
        if (!(entity instanceof CuttingBoardBlockEntity cuttingBoard)) return;
        cuttingBoard.setCuts(cuts);
        cuttingBoard.setItem(item);
    }

}
