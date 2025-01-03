package dev.efekos.cla.block;

import com.mojang.serialization.MapCodec;
import dev.efekos.cla.block.entity.PlateBlockEntity;
import dev.efekos.cla.init.ClaComponentTypes;
import dev.efekos.cla.init.ClaItems;
import dev.efekos.cla.init.ClaSoundEvents;
import dev.efekos.cla.resource.Course;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
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
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateBlock extends BlockWithEntity {

    public static final MapCodec<PlateBlock> CODEC = createCodec(PlateBlock::new);
    private static final VoxelShape shape = VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.125, 0.875);

    public PlateBlock(Settings settings) {
        super(settings);
    }

    private static @NotNull ItemStack createStack(PlateBlockEntity plate) {
        List<ItemStack> stacks = Optional.ofNullable(plate.getItems()).orElse(new ArrayList<>());
        ItemStack stack = new ItemStack(ClaItems.PLATE, 1);
        if (plate.hasCourse()) {
            Course course = plate.getCurrentCourse();
            stack.set(ClaComponentTypes.COURSE_ID, course.id());
            stack.set(DataComponentTypes.FOOD, new FoodComponent(course.nutrition(), course.saturation(), false));
            stack.set(DataComponentTypes.CONSUMABLE, ConsumableComponents.FOOD);
        }
        if (!stacks.isEmpty()) stack.set(ClaComponentTypes.ITEMS, stacks);
        return stack;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos, state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity entity = builder.get(LootContextParameters.BLOCK_ENTITY);
        return entity instanceof PlateBlockEntity plate ? List.of(createStack(plate)) : List.of(ClaItems.PLATE.getDefaultStack());
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (world.isAir(pos.down())) world.breakBlock(pos, true);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.down());
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
        if (player.isSneaking()) {
            if (stacks.isEmpty()) return pickup(world, pos, player, plate, hand);
            ItemScatterer.spawn(world, pos, DefaultedList.copyOf(ItemStack.EMPTY, stacks.toArray(new ItemStack[0])));
            plate.setItems(new ArrayList<>());
            plate.markDirty();
            world.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value(), SoundCategory.BLOCKS);
            return world.isClient ? ActionResult.SUCCESS : ActionResult.SUCCESS_SERVER;
        } else {
            if (plate.hasCourse() && playerStack.isEmpty()) return pickup(world, pos, player, plate, hand);
            if (!plate.acceptsItems(playerStack) || playerStack.isEmpty() || stacks.stream().anyMatch(itemStack -> itemStack.isOf(playerStack.getItem())))
                return ActionResult.PASS;
            stacks.add(playerStack.copyWithCount(1));
            plate.setItems(stacks);
            plate.markDirty();
            playerStack.decrement(1);
            world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS);
            return world.isClient ? ActionResult.SUCCESS : ActionResult.SUCCESS_SERVER;
        }

    }

    private ActionResult pickup(World world, BlockPos pos, PlayerEntity player, PlateBlockEntity plate, Hand hand) {
        ItemStack stack = createStack(plate);
        player.setStackInHand(hand, stack);
        world.playSound(player, pos, ClaSoundEvents.PLATE_PICKUP, SoundCategory.BLOCKS);
        world.removeBlock(pos, false);
        return world.isClient ? ActionResult.SUCCESS : ActionResult.SUCCESS_SERVER;
    }

}
