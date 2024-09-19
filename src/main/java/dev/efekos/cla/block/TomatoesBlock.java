package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class TomatoesBlock extends CropBlock {

    public static final MapCodec<CarrotsBlock> CODEC = createCodec(CarrotsBlock::new);
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0)
    };

    public TomatoesBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public MapCodec<CarrotsBlock> getCodec() {
        return CODEC;
    }

    protected ItemConvertible getSeedsItem() {
        return ClaItems.TOMATO_SEEDS;
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[this.getAge(state)];
    }

}
