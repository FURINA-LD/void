package com.frnc.createvoid.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.createmod.catnip.animation.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * 3D Spout 动画渲染（用于 JEI 配方显示）。
 * <p>
 * 包含 Spout 三个可动部件（顶部/中间/底部）的上下振荡动画、
 * 以及目标方块的 3D 渲染。流体已在 JEI 流体槽中显示。
 * </p>
 */
public class AnimatedSpoutCustomTarget extends AnimatedKinetics {
    private List<FluidStack> fluids = List.of();
    private List<BlockState> targets = List.of();

    public AnimatedSpoutCustomTarget withFluids(List<FluidStack> fluids) {
        this.fluids = fluids;
        return this;
    }

    public AnimatedSpoutCustomTarget withTarget(List<BlockState> targets) {
        this.targets = targets;
        return this;
    }

    @Override
    public void draw(GuiGraphics gfx, int x, int y) {
        PoseStack pose = gfx.pose();
        pose.pushPose();

        // 1. 平移 + 3D 视角旋转
        pose.translate(x, y, 100);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        int scale = 20;

        // 2. 渲染 Spout 主体方块
        defaultBlockElement(AllBlocks.SPOUT.getDefaultState())
                .scale(scale)
                .render(gfx);

        // 3. 振荡动画（周期 30 ticks）
        float time = AnimationTickHolder.getRenderTime();
        float cycle = (time - offset * 8) % 30f;
        float anim = cycle < 20 ? Mth.sin(cycle / 20f * Mth.PI) : 0;
        anim *= 20;

        // 4. 渲染 Spout 三个可动部件（TOP → MIDDLE → BOTTOM 依次向下偏移）
        pose.pushPose();
        defaultBlockElement(AllPartialModels.SPOUT_TOP).scale(scale).render(gfx);
        pose.translate(0, -3 * anim / 32f, 0);
        defaultBlockElement(AllPartialModels.SPOUT_MIDDLE).scale(scale).render(gfx);
        pose.translate(0, -3 * anim / 32f, 0);
        defaultBlockElement(AllPartialModels.SPOUT_BOTTOM).scale(scale).render(gfx);
        pose.popPose();

        // 5. 渲染目标方块（Spout 下方偏移 2 格）
        if (!targets.isEmpty()) {
            DEFAULT_LIGHTING.applyLighting();
            defaultBlockElement(targets.get(0))
                    .atLocal(0, 2, 0)
                    .scale(scale)
                    .render(gfx);
        }

        pose.popPose();
    }

    @Override
    public int getWidth() {
        return 177;
    }

    @Override
    public int getHeight() {
        return 70;
    }
}
