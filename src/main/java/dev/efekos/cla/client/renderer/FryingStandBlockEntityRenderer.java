package dev.efekos.cla.client.renderer;

import dev.efekos.cla.block.entity.FryingStandBlockEntity;
import dev.efekos.cla.client.renderer.bar.ProgressBarRenderer;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaTags;
import dev.efekos.cla.util.IMinecraftClientMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class FryingStandBlockEntityRenderer implements BlockEntityRenderer<FryingStandBlockEntity> {

    public static final SignTextures USED_OIL_SIGN = new SignTextures("oil", "oil_urgent");
    private static final Color cleanOil = new Color(0xf2b118);
    private static final Color badOil = new Color(0x4f3404);
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher renderDispatcher;

    public FryingStandBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.renderDispatcher = ctx.getRenderDispatcher();
    }

    public static int provideItemColor(ItemStack itemStack, int i) {
        return getOilColor(itemStack.getOrDefault(ClaComponentTypes.OIL_CLEANNESS, 100) / 100d).getRGB();
    }

    public static int provideBlockColor(BlockState blockState, BlockRenderView blockRenderView, BlockPos blockPos, int i) {
        if (blockState == null) return 0xf2b118;
        BlockEntity entity = blockRenderView.getBlockEntity(blockPos);
        if (!(entity instanceof FryingStandBlockEntity stand)) return 0xf2b118;

        double ratio = stand.getOilCleanness() / 100d;

        Color color = getOilColor(ratio);
        return color.getRGB();
    }

    private static @NotNull Color getOilColor(double ratio) {
        int initialRed = badOil.getRed();
        int targetRed = cleanOil.getRed();
        int initialGreen = badOil.getGreen();
        int targetGreen = cleanOil.getGreen();
        int initialBlue = badOil.getBlue();
        int targetBlue = cleanOil.getBlue();

        int red = MathHelper.clamp((int) (initialRed * (1 - ratio) + targetRed * ratio), 0, 255);
        int green = MathHelper.clamp((int) (initialGreen * (1 - ratio) + targetGreen * ratio), 0, 255);
        int blue = MathHelper.clamp((int) (initialBlue * (1 - ratio) + targetBlue * ratio), 0, 255);

        return new Color(red, green, blue);
    }

    @Override
    public void render(FryingStandBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();
        int lightLevel = getLightLevel(world, pos);
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        BlockState state = world.getBlockState(pos);

        matrices.push();
        matrices.translate(0, 0, 0);
        matrices.scale(1f, 1f, 1f);
        manager.renderBlock(state, pos, world, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), true, world.getRandom());
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
        if (entity.getMaxTicks()!=0) {
            if(entity.getMaxTicks()<1)return;
            float v = entity.getTicks() / (float) entity.getMaxTicks();
            // cut status
            ProgressBarRenderer barRenderer = ((IMinecraftClientMixin) MinecraftClient.getInstance()).cla$getProgressBarRenderer();

            matrices.push();
            matrices.translate(0.5f, 1.75f, 0.5f);
            matrices.scale(1f, 1f, 1f);
            Camera camera = this.renderDispatcher.camera;
            matrices.multiply(new Quaternionf().rotationYXZ(3.1415927F - camera.getYaw() * 0.017453292F, -camera.getPitch() * 0.017453292F, 0f));
            barRenderer.renderBar(matrices, ProgressBarRenderer.getDefaultTextures(), v, lightLevel);
            matrices.pop();
        }

        //warnings
        if (entity.getOilCleanness() <= 50) {
            matrices.push();
            matrices.translate(0.5f, 1.45f, 0.5f);
            matrices.scale(.4f, .4f, .4f);
            Camera camera = this.renderDispatcher.camera;
            matrices.multiply(new Quaternionf().rotationYXZ(3.1415927F - camera.getYaw() * 0.017453292F, -camera.getPitch() * 0.017453292F, 0f));
            BillboardTextureRenderer.getInstanceFromClient().render(matrices, USED_OIL_SIGN.get(entity.getOilCleanness() <= 10), lightLevel);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));
    }


}
