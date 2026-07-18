package com.frnc.createvoid.datagen;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateVoid.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.VOID_BLOCK.get())
                .add(ModBlocks.ANDESITE_MACHINE.get())
                .add(ModBlocks.COPPER_MACHINE.get())
                .add(ModBlocks.BRASS_MACHINE.get())
                .add(ModBlocks.REDSTONE_MACHINE.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.VOID_BLOCK.get())
                .add(ModBlocks.ANDESITE_MACHINE.get())
                .add(ModBlocks.COPPER_MACHINE.get())
                .add(ModBlocks.BRASS_MACHINE.get())
                .add(ModBlocks.REDSTONE_MACHINE.get());

    }
}
