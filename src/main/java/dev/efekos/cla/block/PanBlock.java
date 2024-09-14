package dev.efekos.cla.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PanBlock extends Block {

    public PanBlock(Settings settings) {
        super(settings);
    }

    public static VoxelShape makeShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.75, 0.0625, 0.1875, 0.8125, 0.125, 0.75),
                VoxelShapes.cuboid(0.25, 0, 0.25, 0.75, 0.0625, 0.75),
                VoxelShapes.cuboid(0.375, 0.0625, -0.25, 0.5625, 0.125, 0.1875),
                VoxelShapes.cuboid(0.1875, 0.0625, 0.1875, 0.75, 0.125, 0.25),
                VoxelShapes.cuboid(0.1875, 0.0625, 0.25, 0.25, 0.125, 0.8125),
                VoxelShapes.cuboid(0.25, 0.0625, 0.75, 0.8125, 0.125, 0.8125)
        );
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

}
