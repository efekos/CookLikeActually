package dev.efekos.cla.block;

import dev.efekos.cla.block.entity.BlockEntityWithOneItem;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWithOneItem extends BlockWithEntity {

    public BlockWithOneItem(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        ItemStack playerStack = player.getStackInHand(hand);
        if (playerStack.isOf(ClaItems.KNIFE)) return ActionResult.PASS;
        if (!(world.getBlockEntity(pos) instanceof BlockEntityWithOneItem entity)) return ActionResult.PASS;

        boolean doesMeterHaveAnItem = entity.hasItem();
        boolean doesPlayerHaveAnItem = !playerStack.isEmpty();

        if (doesMeterHaveAnItem && !doesPlayerHaveAnItem) {
            player.setStackInHand(hand, entity.getItem().copy());
            entity.setItem(ItemStack.EMPTY);
            entity.markDirty();
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1F, 1F, true);
            return ActionResult.SUCCESS;
        } else if (!doesMeterHaveAnItem && doesPlayerHaveAnItem) {
            entity.setItem(playerStack.copyWithCount(1));
            entity.markDirty();
            playerStack.decrement(1);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1F, 1F, true);
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;
    }

}
