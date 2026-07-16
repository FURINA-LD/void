package com.frnc.createvoid.world.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimension {
    // 从主类引用 MOD_ID
    public static final String MOD_ID = "create_void";

    // Dimension type is registered via JSON at: data/create_void/dimension_type/void.json
    public static final ResourceKey<DimensionType> VOID_DIM_TYPE_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(MOD_ID, "void"));

    public static final ResourceKey<Level> VOID_LEVEL_KEY =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "void"));
}