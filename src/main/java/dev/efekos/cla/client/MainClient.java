package dev.efekos.cla.client;

import dev.efekos.cla.block.entity.CuttingBoardBlockEntityRenderer;
import dev.efekos.cla.block.entity.PanBlockEntityRenderer;
import dev.efekos.cla.block.entity.PlateBlockEntityRenderer;
import dev.efekos.cla.block.entity.SyncAbleBlockEntity;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.packet.CuttingBoardSyncS2C;
import dev.efekos.cla.packet.PanSyncS2C;
import dev.efekos.cla.packet.PlateSyncS2C;
import dev.efekos.cla.packet.RequestSyncC2S;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Block Entity Renderers
        BlockEntityRendererFactories.register(ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE, CuttingBoardBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlocks.PLATE_TYPE, PlateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlocks.PAN_BLOCK_ENTITY_TYPE, PanBlockEntityRenderer::new);

        // Packet Receivers
        ClientPlayNetworking.registerGlobalReceiver(CuttingBoardSyncS2C.PAYLOAD_ID, CuttingBoardSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PlateSyncS2C.PAYLOAD_ID, PlateSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PanSyncS2C.PAYLOAD_ID, PanSyncS2C::handle);

        // Events
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::loadBlockEntity);
    }

    private void loadBlockEntity(BlockEntity blockEntity, ClientWorld clientWorld) {
        if (blockEntity instanceof SyncAbleBlockEntity)
            ClientPlayNetworking.send(new RequestSyncC2S(blockEntity.getPos()));
    }

}