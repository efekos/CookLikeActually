package dev.efekos.cla.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.efekos.cla.block.entity.FryingStandBlockEntity;
import dev.efekos.cla.block.entity.PanBlockEntity;
import dev.efekos.cla.init.ClaTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class FryingStandBlockEntityRenderer implements BlockEntityRenderer<FryingStandBlockEntity> {

    private static final Identifier PROGRESS_TEXTURE = Identifier.of("cla", "textures/gui/progress.png");
    private static final Identifier PROGRESS_FULL_TEXTURE = Identifier.of("cla", "textures/gui/progress_full.png");
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher renderDispatcher;


    public FryingStandBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
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
    public void render(FryingStandBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = world.getBlockState(pos);

        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        BakedModel model = manager.getModel(state);

        //block itself
        matrices.push();
        matrices.translate(0, 0, 0);
        matrices.scale(1f, 1f, 1f);
        VertexConsumer solid = vertexConsumers.getBuffer(RenderLayer.getCutout());
        int lightLevel = getLightLevel(world, pos);
        manager.getModelRenderer().render(world, model, state, pos, matrices, solid, false, world.getRandom(), lightLevel, 1);
        matrices.pop();

        if(!entity.hasItem())return;
        ItemStack item = entity.getItem();

        // the item
        if (!item.isEmpty()) {

            boolean isItem = item.isIn(ClaTags.RENDER_AS_ITEM) || !(item.getItem() instanceof BlockItem);

            matrices.push();
            matrices.translate(0.5f, !isItem ? 1.055f : 0.87f, 0.5f);

            matrices.scale(0.6f, 0.6f, 0.6f);
            if (isItem) matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

            itemRenderer.renderItem(item, !isItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI, lightLevel, OverlayTexture.DEFAULT_UV, matrices,
                    vertexConsumers, world, 1);
            matrices.pop();
        }

        // progress bar
        if (!entity.hasRecipe(world) || !entity.getRecipe(world).hasProgressBar()) return;
        float v = entity.getTicks() / (float) entity.getRecipe(world).getTime();

        render(PROGRESS_TEXTURE, matrices, lightLevel, 1);
        if (v > 0) render(PROGRESS_FULL_TEXTURE, matrices, lightLevel, v);
    }

    private void render(Identifier id, MatrixStack matrices, int lightLevel, float v) {
        matrices.push();
        matrices.translate(0.5f, 1.75f, 0.5f);
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


    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
