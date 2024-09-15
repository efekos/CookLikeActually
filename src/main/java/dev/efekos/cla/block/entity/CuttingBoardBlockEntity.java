package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.packet.CuttingBoardSyncS2C;
import dev.efekos.cla.recipe.CuttingRecipe;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class CuttingBoardBlockEntity extends BlockEntityWithOneItem implements SyncAbleBlockEntity<CuttingBoardSyncS2C> {

    private int cuts;
    private CuttingRecipe currentRecipe;
    private int maxCutsNeeded;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlockEntityTypes.CUTTING_BOARD_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(item);
        setCuts(0);
    }

    public void setItemWithoutReset(ItemStack item){
        setItem(item);
    }

    public int getCuts() {
        return cuts;
    }

    public void setCuts(int cuts) {
        this.cuts = cuts;
        if (hasRecipe(world) && cuts == maxCutsNeeded) {
            this.item = this.currentRecipe.getRes();
            this.cuts = 0;
            this.maxCutsNeeded = 0;
            this.currentRecipe = null;
        }
    }

    public int getMaxCutsNeeded() {
        return maxCutsNeeded;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.CUTS, cuts);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        cuts = components.getOrDefault(ClaComponentTypes.CUTS, 0);
    }

    public boolean hasRecipe(World world) {
        if(!hasItem())return false;
        Optional<RecipeEntry<CuttingRecipe>> match = world.getRecipeManager().getFirstMatch(CuttingRecipe.Type.INSTANCE, new SingleStackRecipeInput(getItem()), world);
        if (match.isPresent()) {
            CuttingRecipe valued = match.get().value();
            this.currentRecipe = valued;
            this.maxCutsNeeded = valued.getCuts();
        } else {
            this.currentRecipe = null;
            this.maxCutsNeeded = 0;
        }
        return match.isPresent();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.cuts = nbt.getInt("Cuts");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("Cuts", cuts);
    }

    @Override
    public void markDirty() {
        if (!world.isClient) for (ServerPlayerEntity player : PlayerLookup.tracking(((ServerWorld) world), pos))
            ServerPlayNetworking.send(player, createSyncPacket());
        super.markDirty();
    }

    public CuttingBoardSyncS2C createSyncPacket() {
        return new CuttingBoardSyncS2C(hasItem()?item:ItemStack.EMPTY, cuts, pos);
    }

}
