package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.block.entity.WashingStandBlockEntity;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
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

public class WashingStandSyncS2C implements CustomPayload {

    public static final Id<WashingStandSyncS2C> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "washing_stand_sync_s2c"));
    public static final PacketCodec<RegistryByteBuf, WashingStandSyncS2C> CODEC = CustomPayload.codecOf(WashingStandSyncS2C::write, WashingStandSyncS2C::new);
    private final BlockPos pos;
    private final int progress;
    private final List<ItemStack> items;

    public WashingStandSyncS2C(BlockPos pos, int progress, List<ItemStack> items) {
        this.pos = pos;
        this.progress = progress;
        this.items = items;
    }

    public WashingStandSyncS2C(RegistryByteBuf buf) {
        pos = buf.readBlockPos();
        progress = buf.readInt();
        int size = buf.readInt();
        items = new ArrayList<>();
        for (int i = 0; i < size; i++) items.add(ItemStack.PACKET_CODEC.decode(buf));
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(progress);
        buf.writeInt(items.size());
        for (ItemStack item : items) ItemStack.PACKET_CODEC.encode(buf, item);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    @Environment(EnvType.CLIENT)
    public void handle(ClientPlayNetworking.Context context) {
        BlockEntity entity = context.client().world.getBlockEntity(pos);
        if (!(entity instanceof WashingStandBlockEntity washingStandBlock)) return;
        washingStandBlock.setProgress(progress);
        washingStandBlock.setPlates(items);
    }

}
