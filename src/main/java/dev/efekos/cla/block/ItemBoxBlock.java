package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.ItemBoxBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemBoxBlock extends BlockWithEntity {

    public static final MapCodec<ItemBoxBlock> CODEC = createCodec(ItemBoxBlock::new);

    public ItemBoxBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemBoxBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stackInHand = player.getStackInHand(hand);
        if (!(entity instanceof ItemBoxBlockEntity itemBox)) return ActionResult.PASS;
        return player.isCreative() ? creativeUse(itemBox, world, pos, stackInHand) : survivalUse(itemBox, world, pos, player, stackInHand, hand);
    }

    private ActionResult survivalUse(ItemBoxBlockEntity entity, World world, BlockPos pos, PlayerEntity player, ItemStack playerStack, Hand hand) {
        if (playerStack.isEmpty()) {
            player.setStackInHand(hand, entity.getItem().copy());
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private ActionResult creativeUse(ItemBoxBlockEntity entity, World world, BlockPos pos, ItemStack playerStack) {
        if (playerStack.isEmpty()) return ActionResult.PASS;
        entity.setItem(playerStack);
        entity.markDirty();
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f, true);
        return ActionResult.SUCCESS;
    }

}
