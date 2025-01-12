package dev.efekos.cla.item;

import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;

public class OrderNoteItem extends Item {

    public OrderNoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Optional<Course> course = CourseManager.getInstance().getCourse(stack.get(ClaComponentTypes.COURSE_ID));
        if (course.isPresent()) {
            Course crs = course.get();
            for (Ingredient ingredient : crs.ingredients())
                tooltip.add(ingredient.getMatchingStacks()[0].getName().copy().formatted(Formatting.GRAY));
        }
    }

}
