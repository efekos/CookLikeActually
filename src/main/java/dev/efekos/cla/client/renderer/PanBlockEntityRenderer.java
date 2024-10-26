package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.PanBlockEntity;
import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;
import dev.efekos.cla.init.ClaTags;
import dev.efekos.cla.util.IMinecraftClientMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class PanBlockEntityRenderer implements BlockEntityRenderer<PanBlockEntity> {

    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher renderDispatcher;

    public PanBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
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
    public void render(PanBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        int lightLevel = getLightLevel(world, entity.getPos());

        // the item
        if (entity.hasItem()) {
            ItemStack item = entity.getItem();

            boolean isItem = item.isIn(ClaTags.RENDER_AS_ITEM) || !(item.getItem() instanceof BlockItem);

            matrices.push();
            matrices.translate(0.5f, !isItem ? 0.255f : 0.07f, 0.5f);

            matrices.scale(0.6f, 0.6f, 0.6f);
            if (isItem) matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

            itemRenderer.renderItem(item, !isItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI, lightLevel, OverlayTexture.DEFAULT_UV, matrices,
                    vertexConsumers, world, 1);
            matrices.pop();
        }

        // progress bar
        if (entity.hasRecipe(world) && entity.getRecipe(world).hasProgressBar()) {
            float v = entity.getTicks() / (float) entity.getMaxTicks();
            // cut status
            ProgressBarRenderer barRenderer = ((IMinecraftClientMixin) MinecraftClient.getInstance()).cla$getProgressBarRenderer();

            matrices.push();
            matrices.translate(0.5f, .75f, 0.5f);
            matrices.scale(1f, 1f, 1f);
            Camera camera = this.renderDispatcher.camera;
            matrices.multiply(new Quaternionf().rotationYXZ(3.1415927F - camera.getYaw() * 0.017453292F, -camera.getPitch() * 0.017453292F, 0f));
            barRenderer.renderBar(matrices, ProgressBarRenderer.getDefaultTextures(), v, lightLevel);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
