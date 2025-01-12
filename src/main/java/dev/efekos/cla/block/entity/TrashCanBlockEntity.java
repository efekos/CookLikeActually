package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TrashCanBlockEntity extends BlockEntity {

    private List<ItemStack> items = new ArrayList<>();

    public TrashCanBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.TRASH_CAN, pos, state);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        items = components.getOrDefault(ClaComponentTypes.ITEMS, new ArrayList<>());
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if (containsItems()) componentMapBuilder.add(ClaComponentTypes.ITEMS, items);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("Items", NbtElement.LIST_TYPE)) {
            NbtList Items = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
            this.items = new ArrayList<>();
            try {
                for (NbtElement item : Items) items.add(ItemStack.fromNbt(registryLookup, item).orElseThrow());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (containsItems()) {
            NbtList list = new NbtList();
            for (ItemStack item : items) list.add(item.encode(registryLookup));
            nbt.put("Items", list);
        }
    }

    public boolean containsItems() {
        return items != null && !items.isEmpty();
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

}
