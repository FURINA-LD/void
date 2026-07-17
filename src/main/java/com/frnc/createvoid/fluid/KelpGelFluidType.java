package com.frnc.createvoid.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class KelpGelFluidType extends FluidType {
    // 颜色：深绿不透明，表现粘稠质感
    private static final int COLOR = 0xFF2D3C2C; // 完全不透明，ARGB格式

    public KelpGelFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation("minecraft:block/water_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation("minecraft:block/water_flow");
            }

            @Override
            public int getTintColor() {
                return COLOR;
            }

            public Vector3f modifyFogColor(net.minecraft.client.Camera camera, float partialTick,
                                           net.minecraft.client.renderer.LevelRenderer levelRenderer,
                                           int darkness, Vector3f fluidFogColor) {
                // 粘稠液体：深暗绿雾，水下视野极差
                return new Vector3f(0.12f, 0.18f, 0.10f);
            }
        });
    }
}
