package dev.efekos.cla.item;

import dev.efekos.cla.block.entity.FryingStandBlockEntity;
import dev.efekos.cla.init.ClaItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OilBottleItem extends Item {

    public OilBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockEntity entity = world.getBlockEntity(blockPos);
        if(entity==null)return ActionResult.PASS;
        if(!(entity instanceof FryingStandBlockEntity stand)) return ActionResult.PASS;
        if(stand.getOilCleanness()>50) return ActionResult.PASS;
        stand.setOilCleanness(100);
        stand.markDirty();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        world.playSound(player,blockPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS);
        player.setStackInHand(hand, ClaItems.EMPTY_OIL_BOTTLE.getDefaultStack());
        return ActionResult.success(true);
    }

}
