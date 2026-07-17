package com.frnc.createvoid.recipe;

import com.frnc.createvoid.mixin.BlockSpoutingBehaviourMixin;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * 浇水喷淋行为：当 Create Spout 向方块输出流体时触发。
 * <p>
 * 无状态枚举单例，通过 {@link BlockSpoutingBehaviourMixin} 注入到 Create 的
 * {@link BlockSpoutingBehaviour#get(Level, BlockPos)} 方法中。
 * </p>
 *
 * <h3>执行流程</h3>
 * <ol>
 *   <li>Spout 向方块位置输出流体</li>
 *   <li>Mixin 拦截 {@code BlockSpoutingBehaviour.get()}，返回此实例</li>
 *   <li>Create 调用 {@link #fillBlock(Level, BlockPos, SpoutBlockEntity, FluidStack, boolean)}</li>
 *   <li>遍历所有 watering 配方，匹配方块 + 流体</li>
 *   <li>非模拟模式：替换方块并返回消耗的流体量</li>
 * </ol>
 */
public enum WateringBehaviour implements BlockSpoutingBehaviour {
    INSTANCE;

    /**
     * 仅检查方块是否有匹配的浇水配方（不检查流体）。
     * 用于 Mixin 快速判断是否应接管此方块。
     */
    public static boolean hasRecipe(Level level, BlockState state) {
        for (Recipe<?> r :
                level.getRecipeManager().getAllRecipesFor(RecipeTypes.WATERING.getType())) {

            WateringRecipe recipe = (WateringRecipe) r;
            if (recipe.testTargetBlock(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 精确匹配：方块 + 流体 都匹配才返回配方。
     */
    private static WateringRecipe findRecipe(Level level, FluidStack fluid, BlockState state) {
        for (Recipe<?> r :
                level.getRecipeManager().getAllRecipesFor(RecipeTypes.WATERING.getType())) {

            WateringRecipe recipe = (WateringRecipe) r;
            if (!recipe.testTargetBlock(state)) continue;

            FluidIngredient required = recipe.getRequiredFluid();
            if (required == FluidIngredient.EMPTY || required.getRequiredAmount() <= 0)
                continue;
            if (required.test(fluid)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * 核心方法：由 Create Spout 调用，执行实际的方块填充/替换。
     *
     * @param level   当前世界
     * @param pos     目标方块位置
     * @param spout   Spout 方块实体
     * @param fluid   Spout 输出的流体
     * @param simulate true = 仅模拟（不实际修改世界）
     * @return 消耗的流体量（mB）；0 表示不匹配或无法执行
     */
    @Override
    public int fillBlock(Level level, BlockPos pos, SpoutBlockEntity spout,
                         FluidStack fluid, boolean simulate) {

        // 1. 流体有效性
        if (fluid.getFluid() == null) return 0;

        // 2. 当前方块
        BlockState currentState = level.getBlockState(pos);

        // 3. 查找匹配配方
        WateringRecipe recipe = findRecipe(level, fluid, currentState);
        if (recipe == null) return 0;

        // 4. 二次验证所需流体（防御性检查）
        FluidIngredient required = recipe.getRequiredFluid();
        if (!required.test(fluid)) return 0;

        // 5. 非模拟模式：执行方块替换
        if (!simulate) {
            List<ItemStack> results = recipe.rollResults();
            if (results.isEmpty()) return 0;

            ItemStack result = results.get(0);
            if (result.getItem() instanceof BlockItem blockItem) {
                level.setBlockAndUpdate(pos, blockItem.getBlock().defaultBlockState());
            }
        }

        // 6. 返回消耗的流体量
        return required.getRequiredAmount();
    }
}
