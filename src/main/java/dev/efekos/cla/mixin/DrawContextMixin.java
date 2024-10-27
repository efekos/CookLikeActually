package dev.efekos.cla.mixin;

import dev.efekos.cla.util.IDrawContextMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DrawContext.class)
@Environment(EnvType.CLIENT)
public abstract class DrawContextMixin implements IDrawContextMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private MatrixStack matrices;
    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    public abstract void draw();

    @Override
    @Unique
    public void cla$drawItemWithScale(ItemStack item, int x, int y, float scale) {
        drawItemWithScale(this.client.player, this.client.world, item, x, y, scale);
    }

    @Unique
    private void drawItemWithScale(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, float scale) {
        if (!stack.isEmpty()) {
            BakedModel bakedModel = this.client.getItemRenderer().getModel(stack, world, entity, 0);
            this.matrices.push();
            this.matrices.translate((float) (x + 8), (float) (y + 8), (float) (150));

            try {
                this.matrices.scale(scale, -scale, scale);
                boolean bl = !bakedModel.isSideLit();
                if (bl) {
                    DiffuseLighting.disableGuiDepthLighting();
                }

                this.client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, this.matrices, this.vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
                this.draw();
                if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable var12) {
                CrashReport crashReport = CrashReport.create(var12, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Components", () -> String.valueOf(stack.getComponents()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }

            this.matrices.pop();
        }
    }


}
