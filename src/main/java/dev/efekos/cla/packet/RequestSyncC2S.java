package dev.efekos.cla.packet;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.SyncAbleBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RequestSyncC2S implements CustomPayload {

    public static final Id<RequestSyncC2S> PAYLOAD_ID = new Id<>(Identifier.of(Main.MOD_ID, "request_sync_c2s"));
    public static final PacketCodec<RegistryByteBuf, RequestSyncC2S> CODEC = CustomPayload.codecOf(RequestSyncC2S::write, RequestSyncC2S::new);
    private final BlockPos pos;

    public RequestSyncC2S(BlockPos pos) {
        this.pos = pos;
    }

    public RequestSyncC2S(RegistryByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    private void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

    public void handle(ServerPlayNetworking.Context context) {
        ServerPlayerEntity p = context.player();
        World world = p.getWorld();
        BlockEntity entity = world.getBlockEntity(pos);

        if (entity instanceof SyncAbleBlockEntity<?> cuttingBoard) {
            System.out.println("sending sync");
            ServerPlayNetworking.send(p, cuttingBoard.createSyncPacket());
        }
    }

}
