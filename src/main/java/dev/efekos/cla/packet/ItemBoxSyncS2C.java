package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.ItemBoxBlockEntity;
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

public class ItemBoxSyncS2C implements CustomPayload {

    public static final Id<ItemBoxSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "item_box_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, ItemBoxSyncS2C> CODEC = CustomPayload.codecOf(ItemBoxSyncS2C::write, ItemBoxSyncS2C::new);

    private final ItemStack item;
    private final BlockPos pos;

    public ItemBoxSyncS2C(ItemStack item, BlockPos pos) {
        this.item = item;
        this.pos = pos;
    }

    public ItemBoxSyncS2C(RegistryByteBuf buf) {
        boolean b = buf.readBoolean();
        this.item = b ? ItemStack.PACKET_CODEC.decode(buf) : ItemStack.EMPTY;
        this.pos = buf.readBlockPos();
    }

    public void write(RegistryByteBuf buf) {
        boolean b = this.item != null && !this.item.isEmpty();
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
        if (!(entity instanceof ItemBoxBlockEntity itemBox)) return;
        itemBox.setItem(item);
    }

}
