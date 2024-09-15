package dev.efekos.cla.client;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntityRenderer;
import dev.efekos.cla.block.entity.PanBlockEntityRenderer;
import dev.efekos.cla.block.entity.PlateBlockEntityRenderer;
import dev.efekos.cla.block.entity.SyncAbleBlockEntity;
import dev.efekos.cla.client.item.PlateItemRenderer;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.packet.CuttingBoardSyncS2C;
import dev.efekos.cla.packet.PanSyncS2C;
import dev.efekos.cla.packet.PlateSyncS2C;
import dev.efekos.cla.packet.RequestSyncC2S;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.List;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Block Entity Renderers
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.CUTTING_BOARD_BLOCK_ENTITY_TYPE, CuttingBoardBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.PLATE_TYPE, PlateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.PAN_BLOCK_ENTITY_TYPE, PanBlockEntityRenderer::new);

        // Packet Receivers
        ClientPlayNetworking.registerGlobalReceiver(CuttingBoardSyncS2C.PAYLOAD_ID, CuttingBoardSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PlateSyncS2C.PAYLOAD_ID, PlateSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PanSyncS2C.PAYLOAD_ID, PanSyncS2C::handle);

        // Events
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::loadBlockEntity);
        ModelLoadingPlugin.register(this::loadAndRegisterModels);

        // Builtin Item Renderers
        BuiltinItemRendererRegistry.INSTANCE.register(ClaItems.PLATE,new PlateItemRenderer());
    }

    private void loadAndRegisterModels(ModelLoadingPlugin.Context pluginContext) {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

        List<Identifier> modelFiles = resourceManager.findResources("models/block", path -> path.getNamespace().equals(Main.MOD_ID)).keySet()
                .stream()
                .map(Identifier::getPath)
                .map(s -> s.substring(7,s.length()-5))
                .map(s->Identifier.of(Main.MOD_ID, s))
                .toList();
        pluginContext.addModels(modelFiles);
    }

    private void loadBlockEntity(BlockEntity blockEntity, ClientWorld clientWorld) {
        if (blockEntity instanceof SyncAbleBlockEntity)
            ClientPlayNetworking.send(new RequestSyncC2S(blockEntity.getPos()));
    }

}