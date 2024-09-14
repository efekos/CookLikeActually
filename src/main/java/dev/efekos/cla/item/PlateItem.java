package dev.efekos.cla.item;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PlateItem extends BlockItem {

    public PlateItem(Settings settings) {
        super(ClaBlocks.PLATE, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        ComponentMap components = stack.getComponents();
        if (!components.contains(ClaComponentTypes.ITEMS)) return;
        for (ItemStack itemStack : components.getOrDefault(ClaComponentTypes.ITEMS, new ArrayList<ItemStack>()))
            tooltip.add(itemStack.getName().copy().formatted(Formatting.GRAY));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        Identifier identifier = stack.get(ClaComponentTypes.COURSE_ID);
        if(identifier==null)return super.getTranslationKey(stack);
        Course course = CourseManager.getInstance().getCourse(identifier).orElseThrow();
        return course.translationKey();
    }

}