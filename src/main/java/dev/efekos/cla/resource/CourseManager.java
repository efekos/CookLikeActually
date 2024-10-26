package dev.efekos.cla.resource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.efekos.cla.Main;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.profiler.Profiler;

import java.util.*;

public class CourseManager extends JsonDataLoader<Course> implements IdentifiableResourceReloadListener {

    public static final Identifier ID = Identifier.of(Main.MOD_ID, "course");

    private static CourseManager instance;
    private final Map<Identifier, Course> courses = new HashMap<>();

    public static final Codec<Course> CODEC = RecordCodecBuilder.create(i->i.group(
            Identifier.CODEC.fieldOf("model").forGetter(Course::modelId),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Course::ingredients),
            Codecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(Course::nutrition),
            Codecs.NON_NEGATIVE_INT.fieldOf("saturation").forGetter(Course::saturation),
            Codec.STRING.optionalFieldOf("key").forGetter(course -> Optional.of(course.translationKey())),
            Ingredient.CODEC.listOf().fieldOf("transformers").forGetter(Course::transformers)
    ).apply(i, (mid, ingredients, nutrition, saturation, key, transformers) -> new Course(null,mid,ingredients,nutrition,saturation,key.orElse(null),transformers)));

    public CourseManager(RegistryWrapper.WrapperLookup registries) {
        super(registries, CODEC, "course");
        instance = this;
    }

    public static CourseManager getInstance() {
        return instance;
    }

    @Override
    protected void apply(Map<Identifier, Course> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((identifier, course) ->
            courses.put(identifier,course.copyWithId(identifier).copyWithTranslationKey(course.translationKey()==null?createTranslationKey(identifier):course.translationKey()))
        );
    }

    private String createTranslationKey(Identifier identifier) {
        return "course."+identifier.getNamespace()+"."+identifier.getPath();
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    public Optional<Course> getCourse(Identifier id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Optional<Course> findCourse(List<ItemStack> stacks) {
        if (stacks.isEmpty() || stacks.stream().allMatch(stack -> stack == null || stack.isEmpty()))
            return Optional.empty();
        for (Course course : courses.values()) {
            if (!course.matches(stacks)) continue;
            if (stacks.stream().anyMatch(stack -> course.ingredients().stream().noneMatch(ingredient -> ingredient.test(stack))))
                continue;
            return Optional.of(course);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return IdentifiableResourceReloadListener.super.getFabricDependencies();
    }

    public Collection<Course> all() {
        return courses.values();
    }

}
