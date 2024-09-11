package dev.efekos.cla.block.entity;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CuttingBoardBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(CuttingBoardBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack item = entity.getItem();
        if(item==null)return;

        matrices.push();
        matrices.translate(0.5f,item.getItem() instanceof BlockItem ? 0.355f:0.17f,0.5f);

        matrices.scale(0.4f,0.4f,0.4f);
        if(!(item.getItem() instanceof BlockItem))matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

        itemRenderer.renderItem(item, item.getItem() instanceof BlockItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI,getLightLevel(entity.getWorld(),entity.getPos()), OverlayTexture.DEFAULT_UV,matrices,
                vertexConsumers,entity.getWorld(),1);
        matrices.pop();

    }

    private int getLightLevel(World world, BlockPos pos){
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK,pos),world.getLightLevel(LightType.SKY,pos));
    }

}
