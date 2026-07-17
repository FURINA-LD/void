package com.frnc.createvoid.fluid;

import com.frnc.createvoid.block.ModBlocks;
import com.frnc.createvoid.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class KelpGelFluid extends ForgeFlowingFluid {

    public static final Properties PROPERTIES = new Properties(
            ModFluids.KELP_GEL_TYPE,
            ModFluids.KELP_GEL,
            ModFluids.FLOWING_KELP_GEL
    ).block(ModBlocks.KELP_GEL_BLOCK)
            .bucket(ModItems.KELP_GEL_BUCKET)
            .levelDecreasePerBlock(2)
            .tickRate(20)
            .slopeFindDistance(4)
            .explosionResistance(100F);

    protected KelpGelFluid(Properties properties) {
        super(properties);
    }

    // ---------- 静态流体 ----------
    public static class Source extends KelpGelFluid {
        public Source() {
            super(PROPERTIES);
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
    }

    // ---------- 流动流体 ----------
    public static class Flowing extends KelpGelFluid {
        public Flowing() {
            super(PROPERTIES);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
    }

    // ---------- 可选：覆盖部分行为 ----------
    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false; // 防止被其他液体替换
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 2;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState state) {
        return ModBlocks.KELP_GEL_BLOCK.get().defaultBlockState()
                .setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }
}
