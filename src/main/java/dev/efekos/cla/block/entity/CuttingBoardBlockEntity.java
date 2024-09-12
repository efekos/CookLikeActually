package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.recipe.CuttingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CuttingBoardBlockEntity extends BlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }

    private ItemStack item = ItemStack.EMPTY;
    private int cuts;
    private CuttingRecipe currentRecipe;
    private int maxCutsNeeded;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        setCuts(0);
    }

    public int getCuts() {
        return cuts;
    }

    public void setCuts(int cuts) {
        this.cuts = cuts;
        if(hasRecipe(world)&&cuts==maxCutsNeeded) {
            this.item = this.currentRecipe.getRes();
            this.cuts = 0;
            this.maxCutsNeeded = 0;
            this.currentRecipe = null;
        }
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.ITEM,item);
        componentMapBuilder.add(ClaComponentTypes.CUTS,cuts);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        item = components.getOrDefault(ClaComponentTypes.ITEM,ItemStack.EMPTY);
        cuts = components.getOrDefault(ClaComponentTypes.CUTS,0);
    }

    public void tick(World world, BlockPos blockPos, BlockState blockState) {
    }

    public boolean hasRecipe(World world){
        Optional<RecipeEntry<CuttingRecipe>> match = world.getRecipeManager().getFirstMatch(CuttingRecipe.Type.INSTANCE, new SingleStackRecipeInput(item), world);
        if(match.isPresent()) {
            CuttingRecipe valued = match.get().value();
            this.currentRecipe = valued;
            this.maxCutsNeeded = valued.getCuts();
        } else {
            this.currentRecipe = null;
            this.maxCutsNeeded = 0;
        }
        return match.isPresent();
    }

}
