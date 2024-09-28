package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.PanBlockEntity;
import dev.efekos.cla.block.entity.PotBlockEntity;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaComponentTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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

public class PotBlock extends BlockWithOneItem {

    public static final MapCodec<PotBlock> CODEC = createCodec(PotBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public PotBlock(Settings settings) {
        super(settings);
    }

    public static VoxelShape makeShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
                VoxelShapes.cuboid(0.1875, 0.0625, 0.1875, 0.25, 0.625, 0.8125),
                VoxelShapes.cuboid(0.75, 0.0625, 0.1875, 0.8125, 0.625, 0.8125),
                VoxelShapes.cuboid(0.25, 0.0625, 0.75, 0.75, 0.625, 0.8125),
                VoxelShapes.cuboid(0.25, 0.0625, 0.1875, 0.75, 0.625, 0.25),
                VoxelShapes.cuboid(0.375, 0.5, 0.8125, 0.625, 0.5625, 0.875),
                VoxelShapes.cuboid(0.375, 0.5, 0.125, 0.625, 0.5625, 0.1875)
        );
    }

    public static VoxelShape makeSShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.0625, 0.1875, 0.75, 0.625, 0.25),
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
                VoxelShapes.cuboid(0.1875, 0.0625, 0.1875, 0.25, 0.625, 0.8125),
                VoxelShapes.cuboid(0.75, 0.0625, 0.1875, 0.8125, 0.625, 0.8125),
                VoxelShapes.cuboid(0.25, 0.0625, 0.75, 0.75, 0.625, 0.8125),
                VoxelShapes.cuboid(0.8125, 0.5, 0.375, 0.875, 0.5625, 0.625),
                VoxelShapes.cuboid(0.125, 0.5, 0.375, 0.1875, 0.5625, 0.625)
        );
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return direction==Direction.EAST||direction==Direction.WEST?makeSShape():makeShape();
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
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PotBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ClaBlockEntityTypes.POT, PotBlockEntity::tick);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking()) {
            Hand hand = player.getActiveHand();
            if (!player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;

            PotBlockEntity pan = (PotBlockEntity) world.getBlockEntity(pos);
            ItemStack stack = this.asItem().getDefaultStack();
            stack.set(ClaComponentTypes.TICKS, pan.getTicks());
            if (pan.hasItem()) stack.set(ClaComponentTypes.ITEM, pan.getItem());
            player.setStackInHand(hand, stack);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return ActionResult.SUCCESS;
        } else return super.onUse(state, world, pos, player, hit);
    }
}
