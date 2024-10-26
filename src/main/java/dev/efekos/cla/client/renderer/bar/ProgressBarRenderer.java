package dev.efekos.cla.client.renderer.bar;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class ProgressBarRenderer {

    public static final ProgressBarTextures DEFAULT = new ProgressBarTextures(Identifier.of("cla", "textures/gui/progress.png"), Identifier.of("cla", "textures/gui/progress_full.png"));

    public static ProgressBarTextures getDefaultTextures() {
        return DEFAULT;
    }

    public void renderBar(MatrixStack matrices, ProgressBarTextures textures, float value, int lightLevel) {
        renderSingle(matrices, textures.empty(), 1, lightLevel);
        renderSingle(matrices, textures.full(), value, lightLevel);
    }

    public void renderBar(MatrixStack matrices, ProgressBarTextures textures, float value) {
        renderBar(matrices, textures, value, 200);
    }

    private void renderSingle(MatrixStack matrices, Identifier id, float value, int lightLevel) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
        RenderSystem.setShaderTexture(0, id);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        float x = value - 0.5f;
        buffer.vertex(matrix, -.5f, -.5f, 0).texture(0, 0).normal(0, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, -.5f, 0).texture(value, 0).normal(value, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, .5f, 0).texture(value, 1).normal(value, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, -.5f, .5f, 0).texture(0, 1).normal(0, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

}