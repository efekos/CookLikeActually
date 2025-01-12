package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.resource.Course;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PlateBlockEntityRenderer implements BlockEntityRenderer<PlateBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PlateBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PlateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = world.getBlockState(pos);
        int lightLevel = getLightLevel(world, pos);
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();

        // Course Model
        if (entity.hasCourse()) {
            Course course = entity.getCurrentCourse();
            Identifier modelId = course.modelId();
            BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
            BakedModel bakedModel = modelManager.getModel(modelId);
            if (bakedModel == null) return;
            matrices.push();
            matrices.translate(0, 0, 0);
            matrices.scale(1f, 1f, 1f);
            manager.getModelRenderer().render(world, bakedModel, state, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, world.getRandom(), lightLevel, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        } else
            // Items
            for (int i = 0; i < entity.getItems().size(); i++) {
                ItemStack item = entity.getItems().get(i);

                matrices.push();
                matrices.translate(0.5f, 0.08f + i * 0.03f, 0.5f);
                matrices.scale(0.4f, 0.4f, 0.4f);
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));
                itemRenderer.renderItem(item, ModelTransformationMode.NONE, lightLevel, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, world, world.getRandom().nextBetween(1, 10));
                matrices.pop();
            }

    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }
}
