package dev.efekos.cla.block.entity;

import dev.efekos.cla.block.FryingStandBlock;
import dev.efekos.cla.init.*;
import dev.efekos.cla.packet.FryingStandSyncS2C;
import dev.efekos.cla.recipe.FryingRecipe;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FryingStandBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<FryingStandSyncS2C> {

    public FryingStandBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.FRYING_STAND, pos, state);
    }

    private int ticks;

    public boolean hasRecipe(){
        return hasRecipe(this.world);
    }

    public boolean hasRecipe(World world) {
        if(!hasItem())return false;
        return world.getRecipeManager().getFirstMatch(FryingRecipe.Type.INSTANCE,new SingleStackRecipeInput(item),world).isPresent();
    }

    public FryingRecipe getRecipe(World world){
        return world.getRecipeManager().getFirstMatch(FryingRecipe.Type.INSTANCE,new SingleStackRecipeInput(item),world).map(RecipeEntry::value).orElse(null);
    }

    public void tick(BlockState state){
        if (hasRecipe() && state.get(FryingStandBlock.SIEVE)) {
            ticks++;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), ClaSoundEvents.PAN_COOKING, SoundCategory.BLOCKS, 1f, 1f, true);
            FryingRecipe recipe = getRecipe(world);
            if (getTicks() >= recipe.getTime()) {
                setTicks(0);
                setItem(recipe.getRes());
            }
            markDirty();
        } else if (getTicks() != 0) setTicks(0);
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
        componentMapBuilder.add(ClaComponentTypes.TICKS,ticks);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        ticks = components.getOrDefault(ClaComponentTypes.TICKS,0);
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
        return new FryingStandSyncS2C(hasItem()?item:ItemStack.EMPTY,ticks,pos);
    }

    @Override
    public void markDirty() {
        if(!world.isClient) for(ServerPlayerEntity p: PlayerLookup.tracking(this)) ServerPlayNetworking.send(p,createSyncPacket());
        super.markDirty();
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