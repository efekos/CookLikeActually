package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaSoundEvents;
import dev.efekos.cla.init.ClaTags;
import dev.efekos.cla.packet.PanSyncS2C;
import dev.efekos.cla.recipe.PanningRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PanBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<PanSyncS2C> {

    private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, PanningRecipe> matchGetter;
    private int ticks = 0;
    private int maxTicks = 0;
    private boolean shouldRenderProgressBar;

    public PanBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.PAN, pos, state);
        this.matchGetter = ServerRecipeManager.createCachedMatchGetter(PanningRecipe.Type.INSTANCE);
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
        updateDataForClient();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world instanceof ServerWorld sw && hasRecipe(sw) && world.getBlockState(pos.down()).isIn(ClaTags.COOKING_STANDS)) {
            ticks++;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), ClaSoundEvents.PAN_COOKING, SoundCategory.BLOCKS, 1f, 1f, true);
            PanningRecipe recipe = getRecipe(sw);
            if (getTicks() >= recipe.getTime()) {

                if (world.isClient()) {
                    Box box = new Box(new Vec3d(pos.getX() + .1, pos.getY() + .1, pos.getZ() + .1), new Vec3d(pos.getX() + .9, pos.getY() + .15, pos.getZ() + .9));
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

    public boolean hasRecipe(ServerWorld world) {
        if (!hasItem()) return false;
        return updateDataForClient(matchGetter.getFirstMatch(new SingleStackRecipeInput(getItem()), world).map(RecipeEntry::value).orElse(null)) != null;
    }

    public PanningRecipe getRecipe(ServerWorld world) {
        if (!hasItem()) return null;
        return updateDataForClient(matchGetter.getFirstMatch(new SingleStackRecipeInput(getItem()), world).map(RecipeEntry::value).orElse(null));
    }

    private void updateDataForClient() {
        if (world instanceof ServerWorld sw) updateDataForClient(getRecipe(sw));
    }

    public boolean hasRecipe() {
        return world instanceof ServerWorld sw && hasRecipe(sw);
    }

    @Override
    public PanSyncS2C createSyncPacket() {
        return new PanSyncS2C(hasItem() ? item : ItemStack.EMPTY, ticks, pos, maxTicks, shouldRenderProgressBar);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ticks = components.getOrDefault(ClaComponentTypes.TICKS, 0);
    }

    private PanningRecipe updateDataForClient(PanningRecipe recipe) {
        if (recipe == null) {
            setMaxTicks(0);
            shouldRenderProgressBar = false;
            return null;
        }
        setMaxTicks(recipe.getTime());
        shouldRenderProgressBar = recipe.hasProgressBar();
        return recipe;
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
        if (world != null && world.isClient) return maxTicks;
        return hasRecipe() ? getRecipe((ServerWorld) world).getTime() : 0;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public boolean shouldRenderProgressBar() {
        return maxTicks > 0 && shouldRenderProgressBar;
    }

    public void setItemWithoutReset(ItemStack item) {
        super.setItem(item);
    }

    public void setShouldRenderProgressBar(boolean shouldRenderProgressBar) {
        this.shouldRenderProgressBar = shouldRenderProgressBar;
    }

}