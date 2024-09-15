package dev.efekos.cla.resource;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

import java.util.*;

public class CourseManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    public static final Identifier ID = Identifier.of(Main.MOD_ID, "course");

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger log = LogUtils.getLogger();
    private final RegistryWrapper.WrapperLookup wrapperLookup;

    private static CourseManager instance;

    public static CourseManager getInstance() {
        return instance;
    }

    public CourseManager(RegistryWrapper.WrapperLookup wrapperLookup) {
        super(GSON, "course");
        this.wrapperLookup = wrapperLookup;
        instance = this;
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    private final Map<Identifier, Course> courses = new HashMap<>();

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        this.courses.clear();
        prepared.forEach((identifier, jsonElement) -> {
            try {
                readCourse(identifier,jsonElement);
            } catch (IllegalArgumentException | JsonParseException e){
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
        List<Ingredient> ingredients = new ArrayList<>();

        RegistryOps<JsonElement> ops = this.wrapperLookup.getOps(JsonOps.INSTANCE);

        for (JsonElement element : JsonHelper.getArray(root, "ingredients"))
            ingredients.add(Ingredient.DISALLOW_EMPTY_CODEC.parse(ops, element).getOrThrow());
        courses.put(identifier, new Course(identifier, Identifier.tryParse(model), ingredients, nutrition, saturation, eat_seconds, "course." + identifier.getNamespace() + "." + identifier.getPath()));
    }

    public Optional<Course> getCourse(Identifier id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Optional<Course> findCourse(List<ItemStack> stacks) {
        return courses.values().stream().filter(course -> stacks.stream().noneMatch(itemStack -> course.ingredients().stream().noneMatch(ingredient -> ingredient.test(itemStack))) && course.matches(stacks)).findFirst();
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return IdentifiableResourceReloadListener.super.getFabricDependencies();
    }


}
