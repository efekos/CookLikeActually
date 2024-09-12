package dev.efekos.cla.item;

import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.init.ClaBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class KnifeItem extends SwordItem {

    public KnifeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getHand()!= Hand.MAIN_HAND)return ActionResult.success(false);
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        Optional<CuttingBoardBlockEntity> blockEntityOptional = world.getBlockEntity(pos, ClaBlocks.CUTTING_BOARD_BLOCK_ENTITY_TYPE);
        if(blockEntityOptional.isEmpty())return ActionResult.success(false);
        CuttingBoardBlockEntity blockEntity = blockEntityOptional.get();
        if(blockEntity.hasRecipe(world)) blockEntity.setCuts(blockEntity.getCuts()+1);
        world.playSound(context.getPlayer(),pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS,1f,1f);
        return ActionResult.success(true);
    }

}
