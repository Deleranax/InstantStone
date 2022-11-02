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

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.ToIntFunction;

public class InstantRedstoneTorchBlock extends TorchBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final Map<BlockGetter, List<InstantRedstoneTorchBlock.Toggle>> RECENT_TOGGLES = new WeakHashMap<>();

    public InstantRedstoneTorchBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel(litBlockEmission(7)).sound(SoundType.WOOD), InstantStone.INSTANT_REDSTONE_PARTICLE);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.TRUE));
    }

    public static ToIntFunction<BlockState> litBlockEmission(int p_50760_) {
        return (p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? p_50760_ : 0;
    }

    public void onPlace(@NotNull BlockState p_55724_, @NotNull Level p_55725_, @NotNull BlockPos p_55726_, @NotNull BlockState p_55727_, boolean p_55728_) {
        for(Direction direction : Direction.values()) {
            p_55725_.updateNeighborsAt(p_55726_.relative(direction), this);
        }

    }

    public void onRemove(@NotNull BlockState p_55706_, @NotNull Level p_55707_, @NotNull BlockPos p_55708_, @NotNull BlockState p_55709_, boolean p_55710_) {
        if (!p_55710_) {
            for(Direction direction : Direction.values()) {
                p_55707_.updateNeighborsAt(p_55708_.relative(direction), this);
            }

        }
    }

    public int getSignal(BlockState p_55694_, @NotNull BlockGetter p_55695_, @NotNull BlockPos p_55696_, @NotNull Direction p_55697_) {
        return p_55694_.getValue(LIT) && Direction.UP != p_55697_ ? 15 : 0;
    }

    protected boolean hasNeighborSignal(Level p_55681_, BlockPos p_55682_, BlockState p_55683_) {
        return p_55681_.hasSignal(p_55682_.below(), Direction.DOWN);
    }

    public void tick(@NotNull BlockState p_221949_, @NotNull ServerLevel p_221950_, @NotNull BlockPos p_221951_, @NotNull RandomSource p_221952_) {
        stick(p_221949_, p_221950_, p_221951_);
    }

    public void stick(BlockState p_221949_, Level p_221950_, BlockPos p_221951_) {
        boolean flag = this.hasNeighborSignal(p_221950_, p_221951_, p_221949_);
        List<InstantRedstoneTorchBlock.Toggle> list = RECENT_TOGGLES.get(p_221950_);

        while(list != null && !list.isEmpty() && p_221950_.getGameTime() - (list.get(0)).when > 5L) {
            list.remove(0);
        }

        if (p_221949_.getValue(LIT)) {
            if (flag) {
                p_221950_.setBlock(p_221951_, p_221949_.setValue(LIT, Boolean.FALSE), 3);
                if (isToggledTooFrequently(p_221950_, p_221951_, true)) {
                    p_221950_.levelEvent(1502, p_221951_, 0);
                    p_221950_.scheduleTick(p_221951_, p_221950_.getBlockState(p_221951_).getBlock(), 160);
                }
            }
        } else if (!flag && !isToggledTooFrequently(p_221950_, p_221951_, false)) {
            p_221950_.setBlock(p_221951_, p_221949_.setValue(LIT, Boolean.TRUE), 3);
        }
    }

    public void neighborChanged(BlockState p_55699_, @NotNull Level p_55700_, @NotNull BlockPos p_55701_, @NotNull Block p_55702_, @NotNull BlockPos p_55703_, boolean p_55704_) {
        if (p_55699_.getValue(LIT) == this.hasNeighborSignal(p_55700_, p_55701_, p_55699_)) {
            stick(p_55699_, p_55700_, p_55701_);
        }

    }

    public int getDirectSignal(@NotNull BlockState p_55719_, @NotNull BlockGetter p_55720_, @NotNull BlockPos p_55721_, @NotNull Direction p_55722_) {
        return p_55722_ == Direction.DOWN ? p_55719_.getSignal(p_55720_, p_55721_, p_55722_) : 0;
    }

    public boolean isSignalSource(@NotNull BlockState p_55730_) {
        return true;
    }

    public void animateTick(BlockState p_221954_, @NotNull Level p_221955_, @NotNull BlockPos p_221956_, @NotNull RandomSource p_221957_) {
        if (p_221954_.getValue(LIT)) {
            double d0 = (double)p_221956_.getX() + 0.5D + (p_221957_.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double)p_221956_.getY() + 0.7D + (p_221957_.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double)p_221956_.getZ() + 0.5D + (p_221957_.nextDouble() - 0.5D) * 0.2D;
            p_221955_.addParticle(this.flameParticle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55717_) {
        p_55717_.add(LIT);
    }

    private static boolean isToggledTooFrequently(Level p_55685_, BlockPos p_55686_, boolean p_55687_) {
        List<InstantRedstoneTorchBlock.Toggle> list = RECENT_TOGGLES.computeIfAbsent(p_55685_, (p_55680_) -> Lists.newArrayList());
        if (p_55687_) {
            list.add(new InstantRedstoneTorchBlock.Toggle(p_55686_.immutable(), p_55685_.getGameTime()));
        }

        int i = 0;

        for (Toggle torch$toggle : list) {
            if (torch$toggle.pos.equals(p_55686_)) {
                ++i;
                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class Toggle {
        final BlockPos pos;
        final long when;

        public Toggle(BlockPos p_55734_, long p_55735_) {
            this.pos = p_55734_;
            this.when = p_55735_;
        }
    }
}
