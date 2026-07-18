package com.frnc.createvoid.datagen;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.block.ModBlocks;
import com.frnc.createvoid.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModZhCnLangProvider extends LanguageProvider {
    public ModZhCnLangProvider(PackOutput output) {
        super(output, CreateVoid.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.IRON_MECHANISM.get(), "铁构件");
        add(ModItems.INCOMPLETE_BRASS_MACHINE.get(), "未完成的黄铜机械");
        add(ModItems.KELP_GEL_BUCKET.get(), "海带胶桶");
        add(ModItems.LA_VAGUELETTE.get(), "轻涟");

        add(ModBlocks.VOID_BLOCK.get(), "虚空传送符石");
        add(ModBlocks.ANDESITE_MACHINE.get(), "安山机械");
        add(ModBlocks.COPPER_MACHINE.get(), "铜机械");
        add(ModBlocks.BRASS_MACHINE.get(), "黄铜机械");
        add(ModBlocks.REDSTONE_MACHINE.get(), "红石机械");
        add(ModBlocks.KELP_GEL_BLOCK.get(), "海带胶");

        add("itemGroup.create_void_tab", "机械动力：次级维度");
    }
}
