package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

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
}
