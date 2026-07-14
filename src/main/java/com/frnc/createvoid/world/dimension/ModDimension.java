package com.frnc.createvoid.world.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.OptionalLong;

public class ModDimension {
    // 从主类引用 MOD_ID
    public static final String MOD_ID = "create_void";

    public static final DeferredRegister<DimensionType> DIMENSION_TYPES =
            DeferredRegister.create(Registries.DIMENSION_TYPE, MOD_ID);

    public static final ResourceKey<DimensionType> VOID_DIM_TYPE_KEY =
            ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(MOD_ID, "void"));

    public static final ResourceKey<Level> VOID_LEVEL_KEY =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "void"));

    public static final RegistryObject<DimensionType> VOID_DIM_TYPE =
            DIMENSION_TYPES.register("void", () -> new DimensionType(
                    OptionalLong.empty(),
                    true,
                    false,
                    false,
                    true,
                    10.0,
                    true,
                    true,
                    -64,
                    384,
                    384,
                    BlockTags.INFINIBURN_OVERWORLD,
                    new ResourceLocation("minecraft:overworld"),
                    1.0F,
                    new DimensionType.MonsterSettings(
                            false,              //是否对猪灵安全
                            false,              //是否触发袭击
                            ConstantInt.of(0),  // monsterSpawnLightTest - 怪物生成亮度检测（0 表示完全黑暗）
                            -1                   // monsterSpawnBlockLightLimit - 生成时方块亮度上限
                    )
            ));
}