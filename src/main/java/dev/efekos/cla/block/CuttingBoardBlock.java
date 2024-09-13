package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CuttingBoardBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final MapCodec<CuttingBoardBlock> CODEC = createCodec(CuttingBoardBlock::new);
    private static final VoxelShape shape = VoxelShapes.cuboid(0.25, 0, 0.125, 0.75, 0.0625, 0.875);
    private static final VoxelShape shape2 = VoxelShapes.cuboid(0.125, 0, 0.25, 0.875, 0.0625, 0.75);

    public CuttingBoardBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CuttingBoardBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? shape : shape2;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        ItemStack playerStack = player.getStackInHand(hand);
        if (playerStack.isOf(ClaItems.KNIFE)) return ActionResult.PASS;
        BlockEntity entity = world.getBlockEntity(pos);

        if (!(entity instanceof CuttingBoardBlockEntity cuttingBoard)) return ActionResult.PASS;

        boolean doesMeterHaveAnItem = !cuttingBoard.getItem().isEmpty();
        boolean doesPlayerHaveAnItem = !playerStack.isEmpty();

        if (doesMeterHaveAnItem && !doesPlayerHaveAnItem) {
            player.setStackInHand(hand, cuttingBoard.getItem().copy());
            cuttingBoard.setItem(ItemStack.EMPTY);
            cuttingBoard.markDirty();

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F, 1F, true);

            return ActionResult.success(true);
        } else if (!doesMeterHaveAnItem && doesPlayerHaveAnItem) {
            cuttingBoard.setItem(playerStack.copyWithCount(1));
            cuttingBoard.markDirty();
            playerStack.decrement(1);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1F, 1F, true);
            return ActionResult.success(true);
        } else return ActionResult.PASS;

    }

}
