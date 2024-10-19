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
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FryingStandBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<FryingStandSyncS2C> {

    private int ticks;
    private int oilCleanness;

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

    public void tick(BlockState state, BlockPos pos, World world) {
        if (isOilCleanEnough() && hasRecipe() && state.get(FryingStandBlock.SIEVE)) {
            ticks++;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), ClaSoundEvents.PAN_COOKING, SoundCategory.BLOCKS, 1f, 1f, true);
            FryingRecipe recipe = getRecipe(world);
            if (getTicks() >= recipe.getTime()) {
                if (world.isClient()) {
                    Box box = new Box(new Vec3d(pos.getX() + .25, pos.getY() + .85, pos.getZ() + .25), new Vec3d(pos.getX() + .75, pos.getY() + 1.1, pos.getZ() + .75));
                    for (int i = 0; i < 10; i++) {
                        Vec3d p = findRandomPos(box);
                        world.addParticle(ParticleTypes.FLAME.getType(), p.x, p.y, p.z, 0, 0, 0);
                    }
                }
                setTicks(0);
                setItem(recipe.getRes());
                setOilCleanness(getOilCleanness() - (int) (Math.random() * 10));
            }
            markDirty();
        } else if (getTicks() != 0) setTicks(0);
    }

    private boolean isOilCleanEnough() {
        return oilCleanness > 0;
    }

    public int getOilCleanness() {
        return oilCleanness;
    }

    public void setOilCleanness(int oilCleanness) {
        this.oilCleanness = MathHelper.clamp(oilCleanness, 0, 100);
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
        componentMapBuilder.add(ClaComponentTypes.OIL_CLEANNESS, oilCleanness);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ticks = components.getOrDefault(ClaComponentTypes.TICKS, 0);
        oilCleanness = MathHelper.clamp(components.getOrDefault(ClaComponentTypes.OIL_CLEANNESS, 100), 0, 100);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("Ticks", ticks);
        nbt.putInt("OilCleanness", oilCleanness);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        ticks = nbt.getInt("Ticks");
        oilCleanness = nbt.contains("OilCleanness", NbtElement.INT_TYPE) ? MathHelper.clamp(nbt.getInt("OilCleanness"), 0, 100) : 100;
    }

    @Override
    public FryingStandSyncS2C createSyncPacket() {
        return new FryingStandSyncS2C(hasItem() ? item : ItemStack.EMPTY, ticks, oilCleanness, pos);
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
