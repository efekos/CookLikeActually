package dev.efekos.cla.client.hud;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
import dev.efekos.cla.util.CachedFunction;
import dev.efekos.cla.util.IDrawContextMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class OrderNotesHud implements HudRenderCallback {

    public static final Identifier NOTE_TEXTURE = Identifier.of(Main.MOD_ID, "hud/order_note");
    public static final Identifier SLOT_TEXTURE = Identifier.of(Main.MOD_ID, "hud/order_note_ingredient");

    private static final CachedFunction<Identifier, ItemStack> courseIdToStack = new CachedFunction<>(identifier -> new ItemStack(RegistryEntry.of(ClaItems.PLATE), 1, ComponentChanges.builder().add(ClaComponentTypes.COURSE_ID, identifier).build()));
    private static final CachedFunction<Identifier, List<ItemStack>> courseIdToIngredients = new CachedFunction<>(identifier -> {
        Optional<Course> course = CourseManager.getInstance().getCourse(identifier);
        return course.map(value -> value.ingredients().stream().map(ingredient -> new ItemStack(ingredient.getMatchingItems().getFirst().value(), 1)).toList()).orElseGet(ArrayList::new);
    });

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || player.isCreative() || player.isSpectator()) return;
        PlayerInventory inventory = player.getInventory();
        if (!inventory.contains(stack -> stack.isOf(ClaItems.ORDER_NOTE))) return;
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(ClaItems.ORDER_NOTE)) stacks.add(stack);
        }

        for (int i = 0; i < stacks.size(); i++) {
            int x = i * 80 + 10;
            drawContext.drawGuiTexture(RenderLayer::getGuiTextured, NOTE_TEXTURE, x - 1, 0, 71, 100);

            ItemStack noteStack = stacks.get(i);
            Identifier courseId = noteStack.get(ClaComponentTypes.COURSE_ID);
            if (courseId == null) continue;

            ItemStack plateStack = courseIdToStack.apply(courseId);
            ((IDrawContextMixin) drawContext).cla$drawItemWithScale(plateStack, x + 26, 4, 32f);

            List<ItemStack> ingredientStacks = courseIdToIngredients.apply(courseId);
            int startX = x + 33 - Math.min(4, ingredientStacks.size()) * 8;
            for (int j = 0; j < ingredientStacks.size(); j++) {
                ItemStack stack = ingredientStacks.get(j);
                int x1 = startX + (j % 4) * 17;
                int y = ((int) Math.floor(j / 4d)) * 17 + 36;
                drawContext.drawGuiTexture(RenderLayer::getGuiTextured, SLOT_TEXTURE, x1, y, 16, 16);
                ((IDrawContextMixin) drawContext).cla$drawItemWithScale(stack, x1, y, 12f);
            }
        }

    }
}
