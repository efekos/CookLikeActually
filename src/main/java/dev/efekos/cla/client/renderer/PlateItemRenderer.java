package dev.efekos.cla.client.renderer;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PlateItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    public static final Identifier BASE_ITEM_ID = Identifier.of(Main.MOD_ID, "block/plate_base");

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
        BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();
        boolean leftHanded = MinecraftClient.getInstance().options.getMainArm().getValue() == Arm.LEFT;

        // Item itself
        matrices.push();
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.scale(1f, 1f, 1f);
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(0));
        renderer.renderItem(stack.getItem().getDefaultStack(), mode, leftHanded, matrices, vertexConsumers, light, overlay, modelManager.getModel(BASE_ITEM_ID));
        matrices.pop();

        // Course Model
        if (stack.contains(ClaComponentTypes.COURSE_ID)) {
            Course course = CourseManager.getInstance().getCourse(stack.get(ClaComponentTypes.COURSE_ID)).orElseThrow();
            Identifier modelId = course.modelId();
            BakedModel bakedModel = modelManager.getModel(modelId);
            if (bakedModel == null) return;
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.scale(1f, 1f, 1f);
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(0));
            renderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, bakedModel);
            matrices.pop();
        }

    }

}
