package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.PlateSyncS2C;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PlateBlockEntity extends BlockEntity implements SyncAbleBlockEntity {

    private List<ItemStack> items = new ArrayList<>();

    public PlateBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlocks.PLATE_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.ITEMS, items);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.items = components.get(ClaComponentTypes.ITEMS);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        NbtList list = new NbtList();
        for (ItemStack item : items) {
            list.add(item.encode(registryLookup));
        }
        nbt.put("Items", list);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if(!nbt.contains("Items", NbtElement.COMPOUND_TYPE)) return;
        this.items = new ArrayList<>();
        nbt.getList("Items",NbtElement.COMPOUND_TYPE).forEach(nbtElement ->
            ItemStack.fromNbt(registryLookup,nbtElement).ifPresent(itemStack -> items.add(itemStack))
        );
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public CustomPayload createSyncPacket() {
        return new PlateSyncS2C(items,pos);
    }

}