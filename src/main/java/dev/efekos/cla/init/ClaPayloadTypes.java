package dev.efekos.cla.init;

import dev.efekos.cla.packet.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ClaPayloadTypes {

    public static final CustomPayload.Type<? super RegistryByteBuf, CuttingBoardSyncS2C> CUTTING_BOARD_SYNC_S2C = registers2c(CuttingBoardSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, PlateSyncS2C> PLATE_SYNC = registers2c(PlateSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, PanSyncS2C> PAN_SYNC_S2C = registers2c(PanSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, ItemBoxSyncS2C> ITEM_BOX_SYNC_S2C = registers2c(ItemBoxSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, FryingStandSyncS2C> FRYING_STAND_SYNC_S2C = registers2c(FryingStandSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, WashingStandSyncS2C> WASHING_STAND_SYNC_S2C = registers2c(WashingStandSyncS2C.class);
    public static final CustomPayload.Type<? super RegistryByteBuf, RequestSyncC2S> REQUEST_SYNC_C2S = registerc2s(RequestSyncC2S.class);

    @SuppressWarnings("unchecked")
    private static <T extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T> registerc2s(Class<T> clazz) {
        try {
            PacketCodec<RegistryByteBuf, T> codec = (PacketCodec<RegistryByteBuf, T>) clazz.getDeclaredField("CODEC").get(null);
            CustomPayload.Id<T> id = (CustomPayload.Id<T>) clazz.getDeclaredField("PAYLOAD_ID").get(null);
            return PayloadTypeRegistry.playC2S().register(id, codec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T> registers2c(Class<T> clazz) {
        try {
            PacketCodec<RegistryByteBuf, T> codec = (PacketCodec<RegistryByteBuf, T>) clazz.getDeclaredField("CODEC").get(null);
            CustomPayload.Id<T> id = (CustomPayload.Id<T>) clazz.getDeclaredField("PAYLOAD_ID").get(null);
            return PayloadTypeRegistry.playS2C().register(id, codec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void run() {

    }

}
