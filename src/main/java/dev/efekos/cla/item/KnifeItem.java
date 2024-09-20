package dev.efekos.cla.item;

import dev.efekos.cla.block.entity.CuttingBoardBlockEntity;
import dev.efekos.cla.init.ClaBlockEntityTypes;
import dev.efekos.cla.init.ClaSoundEvents;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
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
        if (context.getHand() != Hand.MAIN_HAND) return ActionResult.PASS;
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        Optional<CuttingBoardBlockEntity> blockEntityOptional = world.getBlockEntity(pos, ClaBlockEntityTypes.CUTTING_BOARD);
        if (blockEntityOptional.isEmpty()) return ActionResult.PASS;
        CuttingBoardBlockEntity blockEntity = blockEntityOptional.get();
        if (!blockEntity.hasRecipe(world)) return ActionResult.PASS;
        blockEntity.setCuts(blockEntity.getCuts() + 1);
        world.playSound(context.getPlayer(), pos, ClaSoundEvents.KNIFE_SLICE, SoundCategory.BLOCKS);
        return ActionResult.success(true);
    }

}
