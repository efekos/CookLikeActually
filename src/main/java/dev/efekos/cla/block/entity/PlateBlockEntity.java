package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.PlateSyncS2C;
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
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateBlockEntity extends BlockEntity implements SyncAbleBlockEntity<PlateSyncS2C> {

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
        this.items = components.getOrDefault(ClaComponentTypes.ITEMS, new ArrayList<>());
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        NbtList list = new NbtList();
        for (ItemStack item : items) list.add(item.encode(registryLookup));
        nbt.put("Items", list);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.items = new ArrayList<>();
        NbtList list = nbt.getList("Items", NbtElement.COMPOUND_TYPE);
        if(list.isEmpty())return;
        for (NbtElement element : list) {
            System.out.println(element);
            Optional<ItemStack> stack = ItemStack.fromNbt(registryLookup, element);
            stack.ifPresent(items::add);
        }
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public PlateSyncS2C createSyncPacket() {
        return new PlateSyncS2C(items, pos);
    }

    @Override
    public void markDirty() {
        if (!world.isClient) for (ServerPlayerEntity player : PlayerLookup.tracking(this))
            ServerPlayNetworking.send(player, createSyncPacket());
        super.markDirty();
    }

}