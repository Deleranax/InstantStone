package fr.dfwightstudio.instantstone;

import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public class InstantRepeaterBlock extends RepeaterBlock {
    public InstantRepeaterBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().sound(SoundType.WOOD).noOcclusion().isSuffocating((blockState, blockGetter, blockPos) -> false));
    }

    @Override
    protected int getDelay(@NotNull BlockState p_55830_) {
        return super.getDelay(p_55830_) - 2;
    }
}
