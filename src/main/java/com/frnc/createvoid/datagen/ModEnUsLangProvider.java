package com.frnc.createvoid.datagen;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.block.ModBlocks;
import com.frnc.createvoid.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModEnUsLangProvider extends LanguageProvider {
    public ModEnUsLangProvider(PackOutput output) {
        super(output, CreateVoid.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // 物品(Items)
        add(ModItems.IRON_MECHANISM.get(), "Iron Mechanism");
        add(ModItems.INCOMPLETE_BRASS_MACHINE.get(), "Incomplete Brass Machine");
        add(ModItems.KELP_GEL_BUCKET.get(), "Kelp Gel Bucket");
        add(ModItems.LA_VAGUELETTE.get(), "La Vaguelette");

        //方块(Blocks)
        add(ModBlocks.VOID_BLOCK.get(), "Void Block");
        add(ModBlocks.ANDESITE_MACHINE.get(), "Andesite Machine");
        add(ModBlocks.COPPER_MACHINE.get(), "Copper Machine");
        add(ModBlocks.BRASS_MACHINE.get(), "Brass Machine");
        add(ModBlocks.REDSTONE_MACHINE.get(), "Redstone Machine");
        add(ModBlocks.KELP_GEL_BLOCK.get(), "Kelp Gel Block");

        add("itemGroup.create_void_tab", "Create Void Tab");
    }
}
