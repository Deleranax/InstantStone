/*
 *     ____           _       __    __     _____ __            ___
 *    / __ \_      __(_)___ _/ /_  / /_   / ___// /___  ______/ (_)___
 *   / / / / | /| / / / __ `/ __ \/ __/   \__ \/ __/ / / / __  / / __ \
 *  / /_/ /| |/ |/ / / /_/ / / / / /_    ___/ / /_/ /_/ / /_/ / / /_/ /
 * /_____/ |__/|__/_/\__, /_/ /_/\__/   /____/\__/\__,_/\__,_/_/\____/
 *                  /____/
 * Copyright (c) 2022-2022 Dwight Studio's Team <support@dwight-studio.fr>
 *
 * This Source Code From is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/ .
 */

package fr.dwightstudio.instantstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InstantRedstoneWallTorchBlock extends InstantRedstoneTorchBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public InstantRedstoneWallTorchBlock() {
        super();
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.TRUE));
    }

    public @NotNull String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    public @NotNull VoxelShape getShape(@NotNull BlockState p_55781_, @NotNull BlockGetter p_55782_, @NotNull BlockPos p_55783_, @NotNull CollisionContext p_55784_) {
        return WallTorchBlock.getShape(p_55781_);
    }

    public boolean canSurvive(@NotNull BlockState p_55762_, @NotNull LevelReader p_55763_, @NotNull BlockPos p_55764_) {
        return Blocks.WALL_TORCH.canSurvive(p_55762_, p_55763_, p_55764_);
    }

    public @NotNull BlockState updateShape(@NotNull BlockState p_55772_, @NotNull Direction p_55773_, @NotNull BlockState p_55774_, @NotNull LevelAccessor p_55775_, @NotNull BlockPos p_55776_, @NotNull BlockPos p_55777_) {
        return Blocks.WALL_TORCH.updateShape(p_55772_, p_55773_, p_55774_, p_55775_, p_55776_, p_55777_);
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_55746_) {
        BlockState blockstate = Blocks.WALL_TORCH.getStateForPlacement(p_55746_);
        return blockstate == null ? null : this.defaultBlockState().setValue(FACING, blockstate.getValue(FACING));
    }

    public void animateTick(BlockState p_221959_, @NotNull Level p_221960_, @NotNull BlockPos p_221961_, @NotNull RandomSource p_221962_) {
        if (p_221959_.getValue(LIT)) {
            Direction direction = p_221959_.getValue(FACING).getOpposite();
            double d0 = 0.27D;
            double d1 = (double)p_221961_.getX() + 0.5D + (p_221962_.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)direction.getStepX();
            double d2 = (double)p_221961_.getY() + 0.7D + (p_221962_.nextDouble() - 0.5D) * 0.2D + 0.22D;
            double d3 = (double)p_221961_.getZ() + 0.5D + (p_221962_.nextDouble() - 0.5D) * 0.2D + 0.27D * (double)direction.getStepZ();
            p_221960_.addParticle(this.flameParticle, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    protected boolean hasNeighborSignal(Level p_55748_, BlockPos p_55749_, BlockState p_55750_) {
        Direction direction = p_55750_.getValue(FACING).getOpposite();
        return p_55748_.hasSignal(p_55749_.relative(direction), direction);
    }

    public int getSignal(BlockState p_55752_, @NotNull BlockGetter p_55753_, @NotNull BlockPos p_55754_, @NotNull Direction p_55755_) {
        return p_55752_.getValue(LIT) && p_55752_.getValue(FACING) != p_55755_ ? 15 : 0;
    }

    public @NotNull BlockState rotate(@NotNull BlockState p_55769_, @NotNull Rotation p_55770_) {
        return Blocks.WALL_TORCH.rotate(p_55769_, p_55770_);
    }

    public @NotNull BlockState mirror(@NotNull BlockState p_55766_, @NotNull Mirror p_55767_) {
        return Blocks.WALL_TORCH.mirror(p_55766_, p_55767_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55779_) {
        p_55779_.add(FACING, LIT);
    }
}
