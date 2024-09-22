package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.WashingStandBlockEntity;
import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;
import dev.efekos.cla.util.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Quaternionf;

public class WashingStandBlockEntityRenderer implements BlockEntityRenderer<WashingStandBlockEntity> {

    private final BlockEntityRenderDispatcher renderDispatcher;

    public WashingStandBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.renderDispatcher = context.getRenderDispatcher();
    }

    @Override
    public void render(WashingStandBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(!entity.hasPlates())return;

        int lightLevel = getLightLevel(entity.getWorld(), entity.getPos());
        // cut status
        ProgressBarRenderer barRenderer = ((IMinecraftClientMixin) MinecraftClient.getInstance()).cla$getProgressBarRenderer();

        matrices.push();
        matrices.translate(0.5f, 1.75f, 0.5f);
        matrices.scale(1f, 1f, 1f);
        Camera camera = this.renderDispatcher.camera;
        matrices.multiply(new Quaternionf().rotationYXZ(-0.017453292F * getBackwardsYaw(camera), -0.017453292F * getNegatedPitch(camera), 0f));
        barRenderer.renderBar(matrices,ProgressBarRenderer.getDefaultTextures(),entity.getProgress()/50f,lightLevel);
        matrices.pop();
    }

    private static float getBackwardsYaw(Camera camera) {
        return camera.getYaw() - 180.0F;
    }

    private static float getNegatedPitch(Camera camera) {
        return -camera.getPitch();
    }


    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
