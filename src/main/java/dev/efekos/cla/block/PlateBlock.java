package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateBlock extends BlockWithEntity {

    public static final MapCodec<PlateBlock> CODEC = createCodec(PlateBlock::new);

    public PlateBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    private static final VoxelShape shape = VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.125, 0.875);

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos,state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity entity = builder.get(LootContextParameters.BLOCK_ENTITY);
        if(entity instanceof PlateBlockEntity plate){
            List<ItemStack> value = Optional.ofNullable(plate.getItems()).orElse(new ArrayList<>());
            ItemStack stack = ClaItems.PLATE.getDefaultStack();

            stack.set(ClaComponentTypes.ITEMS, value);
            return List.of(stack);
        } else return List.of(ClaItems.PLATE.getDefaultStack());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        ItemStack playerStack = player.getStackInHand(hand);
        if (playerStack.isOf(ClaItems.KNIFE)) return ActionResult.PASS;
        BlockEntity entity = world.getBlockEntity(pos);

        if (!(entity instanceof PlateBlockEntity plate)) return ActionResult.PASS;

        List<ItemStack> stacks = Optional.ofNullable(plate.getItems()).orElse(new ArrayList<>());
        if(player.isSneaking()){
            ItemScatterer.spawn(world,pos, DefaultedList.copyOf(ItemStack.EMPTY,stacks.toArray(new ItemStack[0])));
            plate.setItems(new ArrayList<>());
            plate.markDirty();
            world.playSound(pos.getX(),pos.getY(),pos.getZ(),SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(),SoundCategory.BLOCKS,1f,1f,true);
            return ActionResult.SUCCESS;
        } else {
            if(playerStack.isEmpty()||stacks.stream().anyMatch(itemStack -> itemStack.isOf(playerStack.getItem())))return ActionResult.PASS;
            stacks.add(playerStack.copyWithCount(1));
            plate.setItems(stacks);
            plate.markDirty();
            playerStack.decrement(1);
            world.playSound(pos.getX(),pos.getY(),pos.getZ(),SoundEvents.ENTITY_ITEM_PICKUP,SoundCategory.BLOCKS,1.0f,1.0f,true);
            return ActionResult.success(true);
        }

    }

}
