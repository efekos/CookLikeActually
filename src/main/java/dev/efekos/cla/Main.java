package dev.efekos.cla;

import dev.efekos.cla.init.*;
import dev.efekos.cla.packet.CuttingBoardSyncS2C;
import dev.efekos.cla.packet.PanSyncS2C;
import dev.efekos.cla.packet.PlateSyncS2C;
import dev.efekos.cla.packet.RequestSyncC2S;
import dev.efekos.cla.resource.CourseManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class Main implements ModInitializer {

    public static final String MOD_ID = "cla";

    @Override
    public void onInitialize() {
        // Initialization
        ClaComponentTypes.run();
        ClaBlocks.run();
        ClaBlockEntityTypes.run();
        ClaItems.run();
        ClaGroups.run();
        ClaRecipeTypes.run();
        ClaSoundEvents.run();

        // Payloads
        PayloadTypeRegistry.playS2C().register(CuttingBoardSyncS2C.PAYLOAD_ID, CuttingBoardSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(PlateSyncS2C.PAYLOAD_ID, PlateSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(PanSyncS2C.PAYLOAD_ID, PanSyncS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSyncC2S.PAYLOAD_ID, RequestSyncC2S.CODEC);

        // Server Payload Listeners
        ServerPlayNetworking.registerGlobalReceiver(RequestSyncC2S.PAYLOAD_ID, RequestSyncC2S::handle);

        // Resources
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(CourseManager.ID, CourseManager::new);

    }

}