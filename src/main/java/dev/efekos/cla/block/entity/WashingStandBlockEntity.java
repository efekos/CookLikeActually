package dev.efekos.cla.block.entity;

import dev.efekos.cla.block.WashingStandBlock;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.packet.WashingStandSyncS2C;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WashingStandBlockEntity extends BlockEntity implements SyncAbleBlockEntity<WashingStandSyncS2C> {

    public static final int MAX_PROGRESS = 50;
    private List<ItemStack> plates = new ArrayList<>();
    private int progress;
    private int lastInteraction;

    public WashingStandBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.WASHING_STAND, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        plates = new ArrayList<>();
        if (nbt.contains("Plates", NbtElement.LIST_TYPE))
            nbt.getList("Plates", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> ItemStack.fromNbt(registryLookup, nbtElement).ifPresent(plates::add));
        lastInteraction = nbt.contains("LastInteraction", NbtElement.INT_TYPE) ? nbt.getInt("LastInteraction") : 0;
        progress = nbt.contains("Progress", NbtElement.INT_TYPE) ? nbt.getInt("Progress") : 0;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (hasPlates()) {
            NbtList list = new NbtList();
            for (ItemStack plate : getPlates()) list.add(plate.encode(registryLookup));
            nbt.put("Plates", list);
        }
        nbt.putInt("LastInteraction", lastInteraction);
        nbt.putInt("Progress", progress);
    }

    public boolean hasPlates() {
        return plates != null && !plates.isEmpty();
    }

    public List<ItemStack> getPlates() {
        return plates;
    }

    public void setPlates(List<ItemStack> plates) {
        this.plates = plates;
    }

    public boolean hasProgress() {
        return progress > 0;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getLastInteraction() {
        return lastInteraction;
    }

    public void setLastInteraction(int lastInteraction) {
        this.lastInteraction = lastInteraction;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        if (hasPlates()) componentMapBuilder.add(ClaComponentTypes.PLATES, plates);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        plates = components.getOrDefault(ClaComponentTypes.PLATES, new ArrayList<>());
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        updateState(world, pos, state);
        if (!hasPlates()) return;

        if (getLastInteraction() <= 3) {
            progress++;
            if (progress >= MAX_PROGRESS) wash(world, pos);
            markDirty();
        }

        lastInteraction++;
    }

    private void wash(World world, BlockPos pos) {
        if (!hasPlates()) return;
        setProgress(0);
        ItemStack stack = plates.removeLast();
        ItemStack stackToDrop = stack.copyComponentsToNewStack(ClaItems.PLATE, 1);
        world.spawnEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, stackToDrop));

        if(world.isClient()) {
            Box box = new Box(new Vec3d(pos.getX()+.05, pos.getY() + .9, pos.getZ()+.05), new Vec3d(pos.getX() +.95, pos.getY() + 1.05, pos.getZ() +.95));
            for (int i = 0; i < 10; i++) {
                Vec3d p = findRandomPos(box);
                world.addParticle(ParticleTypes.RAIN.getType(), p.x, p.y, p.z, 0, 0, 0);
            }
        }

    }

    private void updateState(World world, BlockPos pos, BlockState state) {
        if (plates.size() > 1)
            world.setBlockState(pos, state.with(WashingStandBlock.PLATES, WashingStandBlock.Plates.MULTIPLE));
        else if (plates.size() == 1)
            world.setBlockState(pos, state.with(WashingStandBlock.PLATES, WashingStandBlock.Plates.SINGULAR));
        else world.setBlockState(pos, state.with(WashingStandBlock.PLATES, WashingStandBlock.Plates.NONE));
    }


    public void addPlate(ItemStack playerStack) {
        plates.add(playerStack);
    }

    @Override
    public WashingStandSyncS2C createSyncPacket() {
        return new WashingStandSyncS2C(pos, progress, plates);
    }

}