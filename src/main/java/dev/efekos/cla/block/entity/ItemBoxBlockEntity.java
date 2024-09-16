package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.ItemBoxSyncS2C;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ItemBoxBlockEntity extends BlockEntity implements SyncAbleBlockEntity<ItemBoxSyncS2C> {

    @Override
    public ItemBoxSyncS2C createSyncPacket() {
        return new ItemBoxSyncS2C(item,pos);
    }

    public ItemBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.ITEM_BOX, pos, state);
    }

    private ItemStack item;

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.item = components.getOrDefault(ClaComponentTypes.ITEM,ItemStack.EMPTY);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if(!this.item.isEmpty()) componentMapBuilder.add(ClaComponentTypes.ITEM, this.item);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if(nbt.contains("Item", NbtElement.COMPOUND_TYPE)) this.item = ItemStack.fromNbt(registryLookup,nbt.getCompound("Item")).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if(!this.item.isEmpty()) nbt.put("Item",item.encode(registryLookup));
    }

    public void setItem(ItemStack item) {
        this.item = item.copyWithCount(1);
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void markDirty() {
        if(!world.isClient) for(ServerPlayerEntity p: PlayerLookup.tracking(this)) ServerPlayNetworking.send(p,createSyncPacket());
        super.markDirty();
    }

    public boolean hasItem() {
        return item!=null&&!item.isEmpty();
    }

}
