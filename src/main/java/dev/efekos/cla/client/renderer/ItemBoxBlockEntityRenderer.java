package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.ItemBoxBlockEntity;
import dev.efekos.cla.init.ClaTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;
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
        if (!entity.hasItem()) return;
        ItemStack item = entity.getItem();

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
