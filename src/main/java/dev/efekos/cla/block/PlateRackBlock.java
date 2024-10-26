package dev.efekos.cla.block;

import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.init.ClaSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
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

import java.util.List;

public class PlateRackBlock extends Block {

    public static final IntProperty PLATES = IntProperty.of("plates", 0, 7);
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    private static final VoxelShape shape1 = VoxelShapes.union(
            VoxelShapes.cuboid(0.4375, 0.125, 0, 0.5625, 0.875, 0.0625),
            VoxelShapes.cuboid(0.4375, 0, 0, 0.5625, 0.125, 1),
            VoxelShapes.cuboid(0.4375, 0.125, 0.9375, 0.5625, 0.875, 1),
            VoxelShapes.cuboid(0.125, 0.125, 0.0625, 0.875, 0.875, 0.9375)
    );

    public PlateRackBlock(Settings settings) {
        super(settings);
    }

    public static VoxelShape makeShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.9375, 0.125, 0.4375, 1, 0.875, 0.5625),
                VoxelShapes.cuboid(0, 0, 0.4375, 1, 0.1875, 0.5625),
                VoxelShapes.cuboid(0, 0.125, 0.4375, 0.0625, 0.875, 0.5625),
                VoxelShapes.cuboid(0.0625, 0.125, 0.125, 0.9375, 0.875, 0.875)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(PLATES);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(FACING) == Direction.WEST || state.get(FACING) == Direction.EAST ? shape1 : makeShape();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        ItemStack stack = new ItemStack(ClaItems.PLATE, state.get(PLATES));
        return List.of(stack, asItem().getDefaultStack());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        ItemStack playerStack = player.getStackInHand(hand);

        Integer i = state.get(PLATES);
        if (playerStack.isEmpty()) {
            if (i == 0) return ActionResult.PASS;
            player.setStackInHand(hand, new ItemStack(ClaItems.PLATE, 1));
            world.setBlockState(pos, state.with(PLATES, i - 1));
            world.playSound(player, pos, ClaSoundEvents.PLATE_PICKUP, SoundCategory.PLAYERS);
            return ActionResult.SUCCESS;
        } else if (playerStack.isOf(ClaItems.PLATE) && !playerStack.contains(ClaComponentTypes.COURSE_ID) && !playerStack.contains(ClaComponentTypes.ITEMS)) {
            if (i == 7) return ActionResult.PASS;
            player.setStackInHand(hand, playerStack.copyWithCount(playerStack.getCount() - 1));
            world.setBlockState(pos, state.with(PLATES, i + 1));
            world.playSound(player, pos, ClaSoundEvents.PLATE_PLACE_TO_RACK, SoundCategory.PLAYERS);
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;

    }
}