package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.PlateSyncS2C;
import dev.efekos.cla.resource.Course;
import dev.efekos.cla.resource.CourseManager;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateBlockEntity extends BlockEntity implements SyncAbleBlockEntity<PlateSyncS2C> {

    private List<ItemStack> items = new ArrayList<>();
    private Course currentCourse;

    public PlateBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.PLATE_TYPE, pos, state);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.ITEMS, items);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.items = components.getOrDefault(ClaComponentTypes.ITEMS, new ArrayList<>());
        Optional<Identifier> identifierOptional = Optional.ofNullable(components.get(ClaComponentTypes.COURSE_ID));
        if(identifierOptional.isEmpty())return;
        this.currentCourse = CourseManager.getInstance().getCourse(identifierOptional.get()).orElseThrow();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if(!items.isEmpty()) {
            NbtList list = new NbtList();
            for (ItemStack item : items) list.add(item.encode(registryLookup));
            nbt.put("Items", list);
        }
        if(this.currentCourse!=null) nbt.putString("Course",this.currentCourse.id().toString());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.items = new ArrayList<>();
        if(nbt.contains("Items",NbtElement.LIST_TYPE)){
            NbtList list = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
            if(list.isEmpty())return;
            for (NbtElement element : list) {
                System.out.println(element);
                Optional<ItemStack> stack = ItemStack.fromNbt(registryLookup, element);
                stack.ifPresent(items::add);
            }
        }
        if(nbt.contains("Course")) this.currentCourse = CourseManager.getInstance().getCourse(Identifier.tryParse(nbt.getString("Course"))).orElseThrow();
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
        checkForCourses();
    }

    @Override
    public PlateSyncS2C createSyncPacket() {
        return new PlateSyncS2C(pos,items,currentCourse);
    }

    @Override
    public void markDirty() {
        if (!world.isClient) for (ServerPlayerEntity player : PlayerLookup.tracking(this))
            ServerPlayNetworking.send(player, createSyncPacket());
        super.markDirty();
    }

    public void checkForCourses(){
        Optional<Course> courseO = CourseManager.getInstance().findCourse(items);
        this.currentCourse = courseO.orElse(null);
    }

    public boolean acceptsItems(){
        return this.currentCourse == null;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public boolean hasCourse(){
        return this.currentCourse != null;
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

}