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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InstantRepeaterBlock extends InstantDiodeBlock {
    public InstantRepeaterBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD).noOcclusion().isSuffocating((blockState, blockGetter, blockPos) -> false));
    }

    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    public @NotNull InteractionResult use(@NotNull BlockState p_55809_, @NotNull Level p_55810_, @NotNull BlockPos p_55811_, Player p_55812_, @NotNull InteractionHand p_55813_, @NotNull BlockHitResult p_55814_) {
        if (!p_55812_.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            return InteractionResult.sidedSuccess(p_55810_.isClientSide);
        }
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_55803_) {
        BlockState blockstate = super.getStateForPlacement(p_55803_);
        return blockstate.setValue(LOCKED, this.isLocked(p_55803_.getLevel(), p_55803_.getClickedPos(), blockstate));
    }

    public @NotNull BlockState updateShape(@NotNull BlockState p_55821_, @NotNull Direction p_55822_, @NotNull BlockState p_55823_, LevelAccessor p_55824_, @NotNull BlockPos p_55825_, @NotNull BlockPos p_55826_) {
        return !p_55824_.isClientSide() && p_55822_.getAxis() != p_55821_.getValue(FACING).getAxis() ? p_55821_.setValue(LOCKED, this.isLocked(p_55824_, p_55825_, p_55821_)) : super.updateShape(p_55821_, p_55822_, p_55823_, p_55824_, p_55825_, p_55826_);
    }

    public boolean isLocked(@NotNull LevelReader p_55805_, @NotNull BlockPos p_55806_, @NotNull BlockState p_55807_) {
        return this.getAlternateSignal(p_55805_, p_55806_, p_55807_) > 0;
    }

    protected boolean isAlternateInput(@NotNull BlockState p_55832_) {
        return isDiode(p_55832_);
    }

    public void animateTick(BlockState p_221964_, @NotNull Level p_221965_, @NotNull BlockPos p_221966_, @NotNull RandomSource p_221967_) {
        if (p_221964_.getValue(POWERED)) {
            Direction direction = p_221964_.getValue(FACING);
            double d0 = (double)p_221966_.getX() + 0.5D + (p_221967_.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)p_221966_.getY() + 0.4D + (p_221967_.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)p_221966_.getZ() + 0.5D + (p_221967_.nextDouble() - 0.5D) * 0.2D;
            float f = -5.0F;
            if (p_221967_.nextBoolean()) {
                f = 1;
            }

            f /= 16.0F;
            double d3 = (double)(f * (float)direction.getStepX());
            double d4 = (double)(f * (float)direction.getStepZ());
            p_221965_.addParticle(DustParticleOptions.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55828_) {
        p_55828_.add(FACING, LOCKED, POWERED);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction == null) return true;

        Direction clockwise = state.getValue(FACING).getClockWise();
        Direction counterClockwise = state.getValue(FACING).getCounterClockWise();

        if (direction.equals(clockwise)) return false;
        return !direction.equals(counterClockwise);
    }
}
