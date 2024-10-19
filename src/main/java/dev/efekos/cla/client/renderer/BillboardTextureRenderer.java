package dev.efekos.cla.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.efekos.cla.util.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class BillboardTextureRenderer {

    public static BillboardTextureRenderer getInstanceFromClient(){
        MinecraftClient client = MinecraftClient.getInstance();
        if(client==null)return null;
        IMinecraftClientMixin clientMixin = (IMinecraftClientMixin) client;
        return clientMixin.cla$getBillboardTextureRenderer();
    }

    public void render(MatrixStack matrices, Identifier id, int lightLevel) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, id);
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        float x = 0.5f;
        buffer.vertex(matrix, -.5f, -.5f, 0).texture(0, 1).normal(0, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, -.5f, 0).texture(1, 1).normal(1, 0, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, x, .5f, 0).texture(1, 0).normal(1, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        buffer.vertex(matrix, -.5f, .5f, 0).texture(0, 0).normal(0, 1, 0).color(1f, 1f, 1f, 1f).light(lightLevel);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

}
