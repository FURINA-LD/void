package com.frnc.createvoid.mixin;

import com.frnc.createvoid.wateringrecipes.WateringBehaviour;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 注入到 Create 的 {@link BlockSpoutingBehaviour#get(Level, BlockPos)} 方法头部。
 * <p>
 * 在 Create 查询方块喷淋行为之前，先检查是否有浇水配方匹配该方块。
 * 如果匹配，直接返回 {@link WateringBehaviour#INSTANCE}，从而触发浇水逻辑。
 * </p>
 * <p>
 * 这是整个浇水系统的入口点：没有此 Mixin，Spout 不会知道某个方块有浇水配方。
 * </p>
 */
@Mixin(value = BlockSpoutingBehaviour.class, remap = false)
public interface BlockSpoutingBehaviourMixin {

    @Inject(
            method = "get(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)" +
                     "Lcom/simibubi/create/api/behaviour/spouting/BlockSpoutingBehaviour;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void createVoid$beforeGet(Level level, BlockPos pos,
                                              CallbackInfoReturnable<BlockSpoutingBehaviour> cir) {
        if (WateringBehaviour.hasRecipe(level, level.getBlockState(pos))) {
            cir.setReturnValue(WateringBehaviour.INSTANCE);
        }
    }
}
