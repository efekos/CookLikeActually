package dev.efekos.cla.client;

import dev.efekos.cla.block.entity.CuttingBoardBlockEntityRenderer;
import dev.efekos.cla.init.ClaBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        BlockEntityRendererFactories.register(ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE, CuttingBoardBlockEntityRenderer::new);

    }

}