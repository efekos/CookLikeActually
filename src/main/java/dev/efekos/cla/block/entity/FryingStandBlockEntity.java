package dev.efekos.cla.block.entity;

import dev.efekos.cla.block.FryingStandBlock;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaSoundEvents;
import dev.efekos.cla.packet.FryingStandSyncS2C;
import dev.efekos.cla.recipe.FryingRecipe;
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

public class FryingStandBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<FryingStandSyncS2C> {

    private int ticks;

    public FryingStandBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.FRYING_STAND, pos, state);
    }

    public boolean hasRecipe() {
        return hasRecipe(this.world);
    }

    public boolean hasRecipe(World world) {
        if (!hasItem()) return false;
        return world.getRecipeManager().getFirstMatch(FryingRecipe.Type.INSTANCE, new SingleStackRecipeInput(item), world).isPresent();
    }

    public FryingRecipe getRecipe(World world) {
        return world.getRecipeManager().getFirstMatch(FryingRecipe.Type.INSTANCE, new SingleStackRecipeInput(item), world).map(RecipeEntry::value).orElse(null);
    }

    public void tick(BlockState state,BlockPos pos,World world) {
        if (hasRecipe() && state.get(FryingStandBlock.SIEVE)) {
            ticks++;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), ClaSoundEvents.PAN_COOKING, SoundCategory.BLOCKS, 1f, 1f, true);
            FryingRecipe recipe = getRecipe(world);
            if (getTicks() >= recipe.getTime()) {
                if(world.isClient()) {
                    Box box = new Box(new Vec3d(pos.getX()+.25, pos.getY() + .85, pos.getZ()+.25), new Vec3d(pos.getX() +.75, pos.getY() + 1.1, pos.getZ() +.75));
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

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.TICKS, ticks);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ticks = components.getOrDefault(ClaComponentTypes.TICKS, 0);
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

    @Override
    public FryingStandSyncS2C createSyncPacket() {
        return new FryingStandSyncS2C(hasItem() ? item : ItemStack.EMPTY, ticks, pos);
    }

    public void setItemWithoutReset(ItemStack item) {
        this.item = item;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(item);
        setTicks(0);
    }

}
