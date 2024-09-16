package dev.efekos.cla.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TrashCanBlock extends Block {

    public TrashCanBlock(Settings settings) {
        super(settings);
    }

    public static VoxelShape makeShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.625, 0.8125),
                VoxelShapes.cuboid(0.125, 0.625, 0.125, 0.875, 0.8125, 0.875)
        );
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if(!player.isCreative()&&!player.isSpectator()&&!player.getStackInHand(hand).isEmpty()){
            player.setStackInHand(hand, Items.AIR.getDefaultStack());
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;
    }

}
