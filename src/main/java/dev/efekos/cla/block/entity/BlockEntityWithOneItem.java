package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityWithOneItem extends BlockEntity {

    protected ItemStack item;

    public BlockEntityWithOneItem(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if (hasItem()) componentMapBuilder.add(ClaComponentTypes.ITEM, item);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        setItem(components.getOrDefault(ClaComponentTypes.ITEM, ItemStack.EMPTY));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (hasItem()) nbt.put("Item", item.encode(registryLookup));
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("Item", NbtElement.COMPOUND_TYPE))
            this.item = ItemStack.fromNbt(registryLookup, nbt.getCompound("Item")).orElse(ItemStack.EMPTY);
    }

    public boolean hasItem() {
        return item != null && !item.isEmpty();
    }

}
