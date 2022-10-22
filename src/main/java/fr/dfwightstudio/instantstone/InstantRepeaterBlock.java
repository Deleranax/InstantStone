package fr.dfwightstudio.instantstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class InstantRepeaterBlock extends RepeaterBlock {
    public InstantRepeaterBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD).noOcclusion().isSuffocating((blockState, blockGetter, blockPos) -> false));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState p_55809_, @NotNull Level p_55810_, @NotNull BlockPos p_55811_, @NotNull Player p_55812_, @NotNull InteractionHand p_55813_, @NotNull BlockHitResult p_55814_) {
        if (!p_55812_.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            return InteractionResult.sidedSuccess(p_55810_.isClientSide);
        }
    }

    @Override
    protected int getDelay(@NotNull BlockState p_55830_) {
        return super.getDelay(p_55830_) - 2;
    }
}
