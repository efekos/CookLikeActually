package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.ItemBoxSyncS2C;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class ItemBoxBlockEntity extends BlockEntity implements SyncAbleBlockEntity<ItemBoxSyncS2C> {

    private ItemStack item;

    public ItemBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.ITEM_BOX, pos, state);
    }

    @Override
    public ItemBoxSyncS2C createSyncPacket() {
        return new ItemBoxSyncS2C(item, pos);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.item = components.getOrDefault(ClaComponentTypes.ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if (!this.item.isEmpty()) componentMapBuilder.add(ClaComponentTypes.ITEM, this.item);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("Item", NbtElement.COMPOUND_TYPE))
            this.item = ItemStack.fromNbt(registryLookup, nbt.getCompound("Item")).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (hasItem()) nbt.put("Item", item.toNbt(registryLookup));
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item.copyWithCount(1);
    }

    public boolean hasItem() {
        return item != null && !item.isEmpty();
    }

}
