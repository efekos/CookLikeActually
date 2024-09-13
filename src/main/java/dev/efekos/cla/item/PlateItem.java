package dev.efekos.cla.item;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PlateItem extends BlockItem {

    public PlateItem(Settings settings) {
        super(ClaBlocks.PLATE, settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.set(ClaComponentTypes.ITEMS,new ArrayList<>());
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        ComponentMap components = stack.getComponents();
        if(!components.contains(ClaComponentTypes.ITEMS))return;
        for (ItemStack itemStack : components.get(ClaComponentTypes.ITEMS)) tooltip.add(itemStack.getName());
    }

}