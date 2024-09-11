package dev.efekos.cla.block.entity;

import dev.efekos.cla.init.ClaBlocks;
import dev.efekos.cla.init.ClaComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlockEntity extends BlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }

    private ItemStack item;
    private int cuts;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getCuts() {
        return cuts;
    }

    public void setCuts(int cuts) {
        this.cuts = cuts;
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(ClaComponentTypes.CUTS,cuts);
        componentMapBuilder.add(ClaComponentTypes.ITEM,item);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        item = components.getOrDefault(ClaComponentTypes.ITEM,ItemStack.EMPTY);
        cuts = components.getOrDefault(ClaComponentTypes.CUTS,0);
    }
}
