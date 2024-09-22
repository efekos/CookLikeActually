package dev.efekos.cla.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.efekos.cla.Main;
import dev.efekos.cla.block.entity.FryingStandBlockEntity;
import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;
import dev.efekos.cla.init.ClaTags;
import dev.efekos.cla.util.IMinecraftClientMixin;
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
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class FryingStandBlockEntityRenderer implements BlockEntityRenderer<FryingStandBlockEntity> {

    public static final Identifier WATER_ID = Identifier.of(Main.MOD_ID, "block/frying_stand_water");
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
        int lightLevel = getLightLevel(world, pos);

        //oil level
        matrices.push();
        matrices.translate(0, 0, 0);
        matrices.scale(1f, 1f, 1f);
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(WATER_ID);
        RenderSystem.setShaderColor(.5f, .5f, 0, 1f);
        manager.getModelRenderer().renderFlat(world, bakedModel, state, pos, matrices, vertexConsumers.getBuffer(MinecraftClient.isFabulousGraphicsOrBetter() ? RenderLayer.getSolid() : RenderLayer.getTranslucent()), false, world.getRandom(), lightLevel, OverlayTexture.DEFAULT_UV);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        matrices.pop();

        if (entity.hasItem()) {
            ItemStack item = entity.getItem();

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
        if (entity.hasRecipe(world) && entity.getRecipe(world).hasProgressBar()) {
            float v = entity.getTicks() / (float) entity.getRecipe(world).getTime();
            // cut status
            ProgressBarRenderer barRenderer = ((IMinecraftClientMixin) MinecraftClient.getInstance()).cla$getProgressBarRenderer();

            matrices.push();
            matrices.translate(0.5f, 1.75f, 0.5f);
            matrices.scale(1f, 1f, 1f);
            Camera camera = this.renderDispatcher.camera;
            matrices.multiply(new Quaternionf().rotationYXZ(-0.017453292F * getBackwardsYaw(camera), -0.017453292F * getNegatedPitch(camera), 0f));
            barRenderer.renderBar(matrices,ProgressBarRenderer.getDefaultTextures(),v,lightLevel);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
