package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.block.entity.PlateBlockEntity;
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

import java.util.ArrayList;
import java.util.List;

public class PlateSyncS2C implements CustomPayload {

    public static final Id<PlateSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID,"plate_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, PlateSyncS2C> CODEC = CustomPayload.codecOf(PlateSyncS2C::write, PlateSyncS2C::new);

    private List<ItemStack> items = new ArrayList<>();
    private final BlockPos pos;

    public PlateSyncS2C(List<ItemStack> items, BlockPos pos) {
        this.items = items;
        this.pos = pos;
    }

    public PlateSyncS2C(RegistryByteBuf buf){
        items = buf.readBoolean()? ItemStack.LIST_PACKET_CODEC.decode(buf) : new ArrayList<>();
        pos = buf.readBlockPos();
    }

    public void write(RegistryByteBuf buf){
        boolean b = items == null || items.isEmpty();
        buf.writeBoolean(!b);
        if(!b) ItemStack.LIST_PACKET_CODEC.encode(buf, items);
        buf.writeBlockPos(pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    @Environment(EnvType.CLIENT)
    public void handle(ClientPlayNetworking.Context context) {
        BlockEntity entity = context.client().world.getBlockEntity(pos);
        if(!(entity instanceof PlateBlockEntity plate))return;
        plate.setItems(items);
    }

}