package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.WashingStandBlockEntity;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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

public class WashingStandBlock extends BlockWithEntity {

    public WashingStandBlock(Settings settings) {
        super(settings);
    }

    public static final MapCodec<WashingStandBlock> CODEC = createCodec(WashingStandBlock::new);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WashingStandBlockEntity(pos,state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ClaBlockEntityTypes.WASHING_STAND,(world1, pos, state1, blockEntity) -> blockEntity.tick(world1,pos,state1));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        ItemStack playerStack = player.getStackInHand(hand).copy();
        if (player.isSpectator()) return ActionResult.PASS;
        WashingStandBlockEntity entity = (WashingStandBlockEntity) world.getBlockEntity(pos);

        if(playerStack.isEmpty()||!playerStack.isOf(ClaItems.DIRTY_PLATE)){
            if(!entity.hasPlates()) return ActionResult.PASS;
            entity.setLastInteraction(0);
            entity.markDirty();
        } else if (!player.isSneaking()){
            if (playerStack.isEmpty()||!playerStack.isOf(ClaItems.DIRTY_PLATE)) return ActionResult.PASS;
            player.setStackInHand(hand, ItemStack.EMPTY);
            entity.addPlate(playerStack);
            entity.markDirty();
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
