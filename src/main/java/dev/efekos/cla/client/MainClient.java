package dev.efekos.cla.client;

import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.SyncAbleBlockEntity;
import dev.efekos.cla.client.hud.OrderNotesHud;
import dev.efekos.cla.client.renderer.*;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.packet.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Block Entity Renderers
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.CUTTING_BOARD, CuttingBoardBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.PLATE, PlateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.PAN, PanBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.POT, PotBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.ITEM_BOX, ItemBoxBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.FRYING_STAND, FryingStandBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ClaBlockEntityTypes.WASHING_STAND, WashingStandBlockEntityRenderer::new);

        // Packet Receivers
        ClientPlayNetworking.registerGlobalReceiver(CuttingBoardSyncS2C.PAYLOAD_ID, CuttingBoardSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PlateSyncS2C.PAYLOAD_ID, PlateSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PanSyncS2C.PAYLOAD_ID, PanSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(PotSyncS2C.PAYLOAD_ID, PotSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(ItemBoxSyncS2C.PAYLOAD_ID, ItemBoxSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(FryingStandSyncS2C.PAYLOAD_ID, FryingStandSyncS2C::handle);
        ClientPlayNetworking.registerGlobalReceiver(WashingStandSyncS2C.PAYLOAD_ID, WashingStandSyncS2C::handle);

        // Events
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(this::loadBlockEntity);
        ModelLoadingPlugin.register(this::loadAndRegisterModels);
        HudRenderCallback.EVENT.register(new OrderNotesHud());

        // Builtin Item Renderers
        BuiltinItemRendererRegistry.INSTANCE.register(ClaItems.PLATE, new PlateItemRenderer());

        // Block Render Layers
        BlockRenderLayerMap.INSTANCE.putBlock(ClaBlocks.TOMATOES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ClaBlocks.LETTUCES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ClaBlocks.WASHING_STAND, RenderLayer.getTranslucent());

    }

    private void loadAndRegisterModels(ModelLoadingPlugin.Context pluginContext) {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

        List<Identifier> modelFiles = resourceManager.findResources("models/block", path -> path.getNamespace().equals(Main.MOD_ID) || path.getPath().contains("course")).keySet()
                .stream()
                .map(Identifier::getPath)
                .map(s -> s.substring(7, s.length() - 5))
                .map(s -> Identifier.of(Main.MOD_ID, s))
                .toList();
        pluginContext.addModels(modelFiles);
    }

    private void loadBlockEntity(BlockEntity blockEntity, ClientWorld clientWorld) {
        if (blockEntity instanceof SyncAbleBlockEntity)
            ClientPlayNetworking.send(new RequestSyncC2S(blockEntity.getPos()));
    }

}