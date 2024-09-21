package dev.efekos.cla.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.efekos.cla.block.entity.WashingStandBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class WashingStandBlockEntityRenderer implements BlockEntityRenderer<WashingStandBlockEntity> {

    private final BlockEntityRenderDispatcher renderDispatcher;

    private static final Identifier PROGRESS_TEXTURE = Identifier.of("cla", "textures/gui/progress.png");
    private static final Identifier PROGRESS_FULL_TEXTURE = Identifier.of("cla", "textures/gui/progress_full.png");

    public WashingStandBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.renderDispatcher = context.getRenderDispatcher();
    }

    @Override
    public void render(WashingStandBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(!entity.hasPlates())return;

        int lightLevel = getLightLevel(entity.getWorld(), entity.getPos());
        render(PROGRESS_TEXTURE,matrices, lightLevel, 1f);
        render(PROGRESS_FULL_TEXTURE,matrices, lightLevel, entity.getProgress()/50f);
    }

    private void render(Identifier id, MatrixStack matrices, int lightLevel, float v) {
        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        matrices.scale(1f, 1f, 1f);
        Camera camera = this.renderDispatcher.camera;
        matrices.multiply(new Quaternionf().rotationYXZ(-0.017453292F * getBackwardsYaw(camera), -0.017453292F * getNegatedPitch(camera), 0f));
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, id);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        float x = v - 0.5f;
        buffer.vertex(matrix, -.5f, -.5f, 0).texture(0, 0).normal(0, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, -.5f, 0).texture(v, 0).normal(v, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, .5f, 0).texture(v, 1).normal(v, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, -.5f, .5f, 0).texture(0, 1).normal(0, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
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
