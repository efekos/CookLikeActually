package dev.efekos.cla.client;

import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntityRenderer;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.packet.CuttingBoardSyncS2C;
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
        BlockEntityRendererFactories.register(ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE, CuttingBoardBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(CuttingBoardSyncS2C.PAYLOAD_ID,CuttingBoardSyncS2C::handle);
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::loadBlockEntity);
    }

    private void loadBlockEntity(BlockEntity blockEntity, ClientWorld clientWorld) {
        if(blockEntity instanceof CuttingBoardBlockEntity) ClientPlayNetworking.send(new RequestSyncC2S(blockEntity.getPos()));
    }

}