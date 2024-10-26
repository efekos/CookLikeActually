package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.FryingStandBlockEntity;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FryingStandBlock extends BlockWithOneItem {

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty SIEVE = BooleanProperty.of("sieve");
    public static final MapCodec<FryingStandBlock> CODEC = createCodec(FryingStandBlock::new);

    public FryingStandBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(SIEVE);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(SIEVE, false);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ActionResult result = super.onUse(state, world, pos, player, hit);
        if (result == ActionResult.SUCCESS) {
            if (state.get(SIEVE)) return result;
            FryingStandBlockEntity entity = (FryingStandBlockEntity) world.getBlockEntity(pos);
            if (entity.hasItem() && entity.getItem().isOf(ClaItems.FRYING_SIEVE)) {
                entity.setItem(ItemStack.EMPTY);
                entity.markDirty();
                world.setBlockState(pos, state.with(SIEVE, true));
            }
        }
        return result;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> stacks = super.getDroppedStacks(state, builder);
        if (state.get(SIEVE)) stacks.add(new ItemStack(ClaItems.FRYING_SIEVE));
        return stacks;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FryingStandBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ClaBlockEntityTypes.FRYING_STAND, (world1, pos, state1, blockEntity) -> blockEntity.tick(state1, pos, world1));
    }

}
