package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlateBlock extends BlockWithEntity {

    public static final MapCodec<PlateBlock> CODEC = createCodec(PlateBlock::new);

    public PlateBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos,state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity entity = builder.get(LootContextParameters.BLOCK_ENTITY);
        if(!(entity instanceof PlateBlockEntity plate))return new ArrayList<>();
        ItemStack stack = new ItemStack(ClaItems.PLATE, 1);
        stack.set(ClaComponentTypes.ITEMS,plate.getItems());
        return List.of(stack);
    }

}
