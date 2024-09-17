package dev.efekos.cla.resource;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.efekos.cla.Main;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;

public class CourseManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    public static final Identifier ID = Identifier.of(Main.MOD_ID, "course");

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger log = LogUtils.getLogger();
    private static CourseManager instance;
    private final RegistryWrapper.WrapperLookup wrapperLookup;
    private final Map<Identifier, Course> courses = new HashMap<>();

    public CourseManager(RegistryWrapper.WrapperLookup wrapperLookup) {
        super(GSON, "course");
        this.wrapperLookup = wrapperLookup;
        instance = this;
    }

    public static CourseManager getInstance() {
        return instance;
    }

    private static @NotNull List<Ingredient> readArray(Identifier identifier, JsonObject root, RegistryOps<JsonElement> ops, String key) {
        List<Ingredient> transformers = new ArrayList<>();

        for (JsonElement element : JsonHelper.getArray(root, key)) {
            DataResult<Ingredient> parse = Ingredient.DISALLOW_EMPTY_CODEC.parse(ops, element);
            if (parse.isError())
                log.error("Could not parse course: '{}'.", identifier, new JsonParseException(parse.error().orElseThrow().message()));
            else transformers.add(parse.getOrThrow());
        }
        return transformers;
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        this.courses.clear();
        prepared.forEach((identifier, jsonElement) -> {
            try {
                readCourse(identifier, jsonElement);
            } catch (IllegalArgumentException | JsonParseException e) {
                log.error("Could not read course '{}'.", identifier, e);
            }
        });
    }

    private void readCourse(Identifier identifier, JsonElement jsonElement) {
        if (!jsonElement.isJsonObject())
            throw new JsonSyntaxException("Expected course '" + identifier + "' to be an object.");
        JsonObject root = jsonElement.getAsJsonObject();

        String model = JsonHelper.getString(root, "model");
        int nutrition = JsonHelper.getInt(root, "nutrition");
        int saturation = JsonHelper.getInt(root, "saturation");
        float eat_seconds = JsonHelper.getFloat(root, "eat_seconds");

        RegistryOps<JsonElement> ops = this.wrapperLookup.getOps(JsonOps.INSTANCE);
        courses.put(identifier, new Course(identifier, Identifier.tryParse(model), readArray(identifier, root, ops, "ingredients"), nutrition, saturation, eat_seconds, "course." + identifier.getNamespace() + "." + identifier.getPath(), root.has("transformers") ? readArray(identifier, root, ops, "transformers") : new ArrayList<>()));
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
