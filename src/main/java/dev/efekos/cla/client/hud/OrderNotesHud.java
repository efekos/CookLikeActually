package dev.efekos.cla.client.hud;

import dev.efekos.cla.Main;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.ComponentChanges;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class OrderNotesHud implements HudRenderCallback {

    public static final Identifier NOTE_TEXTURE = Identifier.of(Main.MOD_ID,"hud/order_note");
    public static final Identifier SLOT_TEXTURE = Identifier.of(Main.MOD_ID,"hud/order_note_ingredient");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if(player == null) return;
        PlayerInventory inventory = player.getInventory();
        if (!inventory.contains(stack -> stack.isOf(ClaItems.ORDER_NOTE))) return;
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            ItemStack stack = inventory.getStack(i);
            if(stack==null||stack.isEmpty())continue;
            if(stack.isOf(ClaItems.ORDER_NOTE))stacks.add(stack);
        }

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack noteStack = stacks.get(i);
            Identifier courseId = noteStack.get(ClaComponentTypes.COURSE_ID);
            drawContext.drawGuiTexture(NOTE_TEXTURE,i*80+10,0,70,100);
            ItemStack plateStack = new ItemStack(RegistryEntry.of(ClaItems.PLATE), 1, ComponentChanges.builder().add(ClaComponentTypes.COURSE_ID, courseId).build());
            drawContext.drawItem(plateStack,25,8);
        }

    }
}
