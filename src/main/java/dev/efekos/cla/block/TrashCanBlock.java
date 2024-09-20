package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.TrashCanBlockEntity;
import dev.efekos.cla.init.ClaSoundEvents;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrashCanBlock extends BlockWithEntity {

    public static final MapCodec<TrashCanBlock> CODEC = createCodec(TrashCanBlock::new);

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
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrashCanBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        TrashCanBlockEntity can = (TrashCanBlockEntity) builder.get(LootContextParameters.BLOCK_ENTITY);
        List<ItemStack> stacks = super.getDroppedStacks(state, builder);
        if (can.containsItems()) stacks.addAll(can.getItems());
        return stacks;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        ItemStack playerStack = player.getStackInHand(hand).copy();
        if (player.isSpectator()) return ActionResult.PASS;
        TrashCanBlockEntity entity = (TrashCanBlockEntity) world.getBlockEntity(pos);
        if (!player.isSneaking()) {
            if (playerStack.isEmpty()) return ActionResult.PASS;
            player.setStackInHand(hand, ItemStack.EMPTY);
            entity.addItem(playerStack);
            entity.markDirty();
            world.playSound(player, pos, ClaSoundEvents.TRASH_CAN_PUT_THRASH, SoundCategory.BLOCKS);
        } else {
            if (!playerStack.isEmpty()||!entity.containsItems()) return ActionResult.PASS;
            List<ItemStack> items = entity.getItems();
            ItemStack stack = items.removeLast();
            player.setStackInHand(hand, stack);
            entity.setItems(items);
            entity.markDirty();
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS);
        }
        return ActionResult.SUCCESS;
    }

}
