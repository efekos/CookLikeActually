package dev.efekos.cla.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private static final Identifier identifier = Identifier.of("cla", "textures/gui/progress.png");
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher renderDispatcher;


    public CuttingBoardBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.renderDispatcher = ctx.getRenderDispatcher();
    }

    private static float getBackwardsYaw(Camera camera) {
        return camera.getYaw() - 180.0F;
    }

    private static float getNegatedPitch(Camera camera) {
        return -camera.getPitch();
    }

    @Override
    public void render(CuttingBoardBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack item = entity.getItem();
        if (item == null) return;

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = world.getBlockState(pos);

        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        BakedModel model = manager.getModel(state);

        //block itself
        matrices.push();
        matrices.translate(0, 0, 0);
        matrices.scale(1f, 1f, 1f);
        VertexConsumer solid = vertexConsumers.getBuffer(RenderLayer.getSolid());
        manager.getModelRenderer().render(world, model, state, pos, matrices, solid, false, world.getRandom(), getLightLevel(world, pos), 1);
        matrices.pop();

        // the item
        matrices.push();
        matrices.translate(0.5f, item.getItem() instanceof BlockItem ? 0.255f : 0.07f, 0.5f);

        matrices.scale(0.4f, 0.4f, 0.4f);
        if (!(item.getItem() instanceof BlockItem)) matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

        itemRenderer.renderItem(item, item.getItem() instanceof BlockItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI, getLightLevel(world, pos), OverlayTexture.DEFAULT_UV, matrices,
                vertexConsumers, world, 1);
        matrices.pop();

        if (!entity.hasRecipe(world)) return;
        double v = entity.getCuts() / (double) entity.getMaxCutsNeeded();

        // cut status
        matrices.push();

        Camera camera = this.renderDispatcher.camera;

        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.scale(1f, 1f, 1f);
        matrices.multiply(new Quaternionf().rotationYXZ(-0.017453292F * getBackwardsYaw(camera), -0.017453292F * getNegatedPitch(camera), 0f));
        drawTexture(matrices, identifier, 0, 0, 0, 0, (int) (v * 24), 6, 32, 32);
        matrices.pop();

    }

    public void drawTexture(MatrixStack matrices, Identifier texture, int x, int y, int u, int v, int width, int height) {
        this.drawTexture(matrices, texture, x, y, 0, (float) u, (float) v, width, height, 256, 256);
    }

    public void drawTexture(MatrixStack matrices, Identifier texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        this.drawTexture(matrices, texture, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    public void drawTexture(MatrixStack matrices, Identifier texture, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        this.drawTexture(matrices, texture, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public void drawTexture(MatrixStack matrices, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        this.drawTexture(matrices, texture, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    void drawTexture(MatrixStack matrices, Identifier texture, int x1, int x2, int y1, int y2, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        this.drawTexturedQuad(matrices, texture, x1, x2, y1, y2, z, (u + 0.0F) / (float) textureWidth, (u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) regionHeight) / (float) textureHeight);
    }

    void drawTexturedQuad(MatrixStack matrices, Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z).texture(u1, v1);
        bufferBuilder.vertex(matrix4f, (float) x1, (float) y2, (float) z).texture(u1, v2);
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y2, (float) z).texture(u2, v2);
        bufferBuilder.vertex(matrix4f, (float) x2, (float) y1, (float) z).texture(u2, v1);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }


    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
