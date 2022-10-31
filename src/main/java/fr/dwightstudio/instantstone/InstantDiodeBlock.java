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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;

public abstract class InstantDiodeBlock extends DiodeBlock {

        protected InstantDiodeBlock(Properties p_52499_) {
                super(p_52499_);
        }

        @Override
        protected final int getDelay(@NotNull BlockState p_52584_) {
                return 0;
        }

        @Override
        public void tick(@NotNull BlockState p_221065_, @NotNull ServerLevel p_221066_, @NotNull BlockPos p_221067_, @NotNull RandomSource p_221068_) {
                sTick(p_221065_, p_221066_, p_221067_);
        }

        public void sTick(BlockState p_221065_, Level p_221066_, BlockPos p_221067_) {
                if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
                        boolean flag = p_221065_.getValue(POWERED);
                        boolean flag1 = this.shouldTurnOn(p_221066_, p_221067_, p_221065_);
                        if (flag && !flag1) {
                                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWERED, Boolean.FALSE), 2);
                        } else if (!flag && flag1) {
                                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWERED, Boolean.TRUE), 2);
                        }

                }
        }

        @Override
        protected void checkTickOnNeighbor(@NotNull Level p_52577_, @NotNull BlockPos p_52578_, @NotNull BlockState p_52579_) {
                if (!this.isLocked(p_52577_, p_52578_, p_52579_)) {
                        boolean flag = p_52579_.getValue(POWERED);
                        boolean flag1 = this.shouldTurnOn(p_52577_, p_52578_, p_52579_);
                        if (flag != flag1) {
                                this.sTick(p_52579_, p_52577_, p_52578_);
                        }

                }
        }

        @Override
        public void setPlacedBy(@NotNull Level p_52506_, @NotNull BlockPos p_52507_, @NotNull BlockState p_52508_, @NotNull LivingEntity p_52509_, @NotNull ItemStack p_52510_) {
                if (this.shouldTurnOn(p_52506_, p_52507_, p_52508_)) sTick(p_52508_, p_52506_, p_52507_);
        }

        @Override
        public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_52501_) {
                return super.getStateForPlacement(p_52501_).setValue(POWERED, false);
        }
}
