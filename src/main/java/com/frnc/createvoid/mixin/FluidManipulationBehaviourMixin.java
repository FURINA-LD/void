package com.frnc.createvoid.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.fluids.transfer.FluidManipulationBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

/**
 * 流体操作行为优化 Mixin。
 * <p>
 * 对 Create 的 FluidManipulationBehaviour（Spout/Hose Pulley 的流体搜索基类）做两处优化：
 * </p>
 *
 * <h3>1. 扩大操作范围（{@code redirectMaxBlocks}）</h3>
 * <p>
 * 将 search() 中的最大方块搜索数提升 20%（向上取整），
 * 使 Spout 能够扫描更远的流体源来满足配方需求。
 * </p>
 *
 * <h3>2. 跳过流动末端薄水（{@code wrapAccept}）</h3>
 * <p>
 * 在 search() 标记可接收流体位置时，拦截 BiConsumer：
 * 若目标位置不是水源(source)，且下方也不是水源，且上下流体类型相同，
 * 则跳过该位置。防止 Spout 消耗流动水的薄层末端，避免流体源被破坏。
 * </p>
 */
@Mixin(value = FluidManipulationBehaviour.class, remap = false)
public abstract class FluidManipulationBehaviourMixin {

    /**
     * 将 maxBlocks() 返回值扩大 1.2 倍（向上取整）。
     */
    @ModifyReturnValue(method = "maxBlocks", at = @At("RETURN"), remap = false)
    private int redirectMaxBlocks(int original) {
        return Mth.ceil(original * 1.2f);
    }

    /**
     * 包裹 search() 内部的 BiConsumer.accept(BlockPos, Integer) 调用，
     * 跳过流动水薄层（非水源 + 下方非水源 + 相同流体类型）。
     */
    @WrapOperation(
            method = "search",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"
            ),
            remap = false
    )
    @SuppressWarnings("unchecked")
    protected void wrapAccept(BiConsumer<BlockPos, Integer> instance,
                              Object posObj, Object amountObj,
                              Operation<Void> original) {
        BlockPos pos = (BlockPos) posObj;
        // 通过转换到父类来调用 getWorld()（Mixin AP 无法直接解析继承方法）
        Level level = ((com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour) (Object) this)
                .getWorld();

        FluidState targetFluid = level.getFluidState(pos);
        FluidState belowFluid = level.getFluidState(pos.below());

        // 跳过流动水薄层：目标非水源 && 下方非水源 && 相同流体类型
        if (!targetFluid.isSource()
                && !belowFluid.isEmpty()
                && !belowFluid.isSource()
                && belowFluid.getType().isSame(targetFluid.getType())) {
            return;
        }

        original.call(instance, posObj, amountObj);
    }

}
