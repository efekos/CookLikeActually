package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.ItemBoxBlockEntity;
import dev.efekos.cla.init.ClaTags;
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
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ItemBoxBlockEntityRenderer implements BlockEntityRenderer<ItemBoxBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ItemBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ItemBoxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
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

        ItemStack item = entity.getItem();
        if (item == null) return;
        //item

        if (!item.isEmpty()) {

            boolean isItem = item.isIn(ClaTags.RENDER_AS_ITEM) || !(item.getItem() instanceof BlockItem);

            matrices.push();
            matrices.translate(0.5f, !isItem ? 1.205f : 1.02f, 0.5f);
            matrices.scale(0.8f, 0.8f, 0.8f);
            if (isItem) matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

            itemRenderer.renderItem(item, !isItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI, 200, OverlayTexture.DEFAULT_UV, matrices,
                    vertexConsumers, world, 1);
            matrices.pop();
        }

    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }

}
