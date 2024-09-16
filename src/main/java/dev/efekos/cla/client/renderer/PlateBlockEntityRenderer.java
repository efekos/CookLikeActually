package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.resource.Course;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
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

        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        BakedModel model = manager.getModel(state);

        //block itself
        matrices.push();
        matrices.translate(0, 0, 0);
        matrices.scale(1f, 1f, 1f);
        VertexConsumer solid = vertexConsumers.getBuffer(RenderLayer.getSolid());
        int lightLevel = getLightLevel(world, pos);
        manager.getModelRenderer().render(world, model, state, pos, matrices, solid, false, world.getRandom(), lightLevel, 1);
        matrices.pop();

        // Items
        if (entity.acceptsItems()) for (int i = 0; i < entity.getItems().size(); i++) {
            ItemStack item = entity.getItems().get(i);

            matrices.push();
            matrices.translate(0.5f, 0.08f+i*0.03f, 0.5f);
            matrices.scale(0.4f, 0.4f, 0.4f);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));
            itemRenderer.renderItem(item, ModelTransformationMode.NONE, getLightLevel(world, pos), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, world, 1);
            matrices.pop();
        }


        // Course Model
        if (entity.hasCourse()) {
            Course course = entity.getCurrentCourse();
            Identifier modelId = course.modelId();
            BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager() ;
            BakedModel bakedModel = modelManager.getModel(modelId);
            //TODO: modelId is always null, find a different way to get models
            if (bakedModel == null) return;
            matrices.push();
            matrices.translate(0, 0, 0);
            matrices.scale(1f, 1f, 1f);
            manager.getModelRenderer().render(world, bakedModel, state, pos, matrices, solid, false, world.getRandom(), lightLevel, 1);
            matrices.pop();
        }

    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }
}
