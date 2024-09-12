package dev.efekos.cla.block.entity;

import dev.efekos.cla.block.CuttingBoardBlock;
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
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.Locale;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CuttingBoardBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(CuttingBoardBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack item = entity.getItem();
        if(item==null)return;

        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        BlockState state = world.getBlockState(pos);

        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        BakedModel model = manager.getModel(state);

        //block itself
        matrices.push();
        matrices.translate(0,0,0);
        matrices.scale(1f,1f,1f);
        manager.getModelRenderer().render(world,model,state,pos,matrices,vertexConsumers.getBuffer(RenderLayer.getSolid()),false, world.getRandom(),getLightLevel(world, pos),1);
        matrices.pop();

        // the item
        matrices.push();
        matrices.translate(0.5f,item.getItem() instanceof BlockItem ? 0.255f:0.07f,0.5f);

        matrices.scale(0.4f,0.4f,0.4f);
        if(!(item.getItem() instanceof BlockItem))matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(270));

        itemRenderer.renderItem(item, item.getItem() instanceof BlockItem ? ModelTransformationMode.NONE : ModelTransformationMode.GUI,getLightLevel(world, pos), OverlayTexture.DEFAULT_UV,matrices,
                vertexConsumers, world,1);
        matrices.pop();

    }

    private int getLightLevel(World world, BlockPos pos){
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK,pos),world.getLightLevel(LightType.SKY,pos));
    }

}
