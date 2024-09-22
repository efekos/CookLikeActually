package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaSoundEvents;
import dev.efekos.cla.packet.PanSyncS2C;
import dev.efekos.cla.recipe.PanningRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class PanBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<PanSyncS2C> {

    private int ticks = 0;

    public PanBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.PAN, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, PanBlockEntity panBlockEntity) {
        panBlockEntity.tick(world, blockPos, blockState);
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(item);
        setTicks(0);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasRecipe(world) && world.getBlockState(pos.down()).isOf(ClaBlocks.COOKING_STAND)) {
            ticks++;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), ClaSoundEvents.PAN_COOKING, SoundCategory.BLOCKS, 1f, 1f, true);
            PanningRecipe recipe = getRecipe(world);
            if (getTicks() >= recipe.getTime()) {

                if(world.isClient()) {
                    Box box = new Box(new Vec3d(pos.getX()+.1, pos.getY() + .1, pos.getZ()+.1), new Vec3d(pos.getX() +.9, pos.getY() + .15, pos.getZ() +.9));
                    for (int i = 0; i < 10; i++) {
                        Vec3d p = findRandomPos(box);
                        world.addParticle(ParticleTypes.FLAME.getType(), p.x, p.y, p.z, 0, 0, 0);
                    }
                }

                setTicks(0);
                setItem(recipe.getRes());
            }
            markDirty();
        } else if (getTicks() != 0) setTicks(0);
    }

    private Vec3d findRandomPos(Box box){
        Random random = new Random();
        double x = random.nextDouble(box.minX,box.maxX);
        double y = random.nextDouble(box.minY,box.maxY);
        double z = random.nextDouble(box.minZ,box.maxZ);
        return new Vec3d(x,y,z);
    }

    public boolean hasRecipe(World world) {
        if (!hasItem()) return false;
        return world.getRecipeManager().getFirstMatch(PanningRecipe.Type.INSTANCE, new SingleStackRecipeInput(getItem()), world).isPresent();
    }

    public PanningRecipe getRecipe(World world) {
        if (!hasItem()) return null;
        return world.getRecipeManager().getFirstMatch(PanningRecipe.Type.INSTANCE, new SingleStackRecipeInput(getItem()), world).map(RecipeEntry::value).orElse(null);
    }

    public boolean hasRecipe() {
        return hasRecipe(world);
    }

    @Override
    public PanSyncS2C createSyncPacket() {
        return new PanSyncS2C(hasItem() ? item : ItemStack.EMPTY, ticks, pos);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ticks = components.getOrDefault(ClaComponentTypes.TICKS, 0);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.TICKS, ticks);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("Ticks", ticks);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        ticks = nbt.getInt("Ticks");
    }

    public int getMaxTicks() {
        return hasRecipe() ? getRecipe(world).getTime() : 0;
    }

    public void setItemWithoutReset(ItemStack item) {
        super.setItem(item);
    }
}