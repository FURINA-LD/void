package com.frnc.createvoid.datagen;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateVoid.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.VOID_BLOCK.get(), cubeAll(ModBlocks.VOID_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.ANDESITE_MACHINE.get(), cubeAll(ModBlocks.ANDESITE_MACHINE.get()));
        simpleBlockWithItem(ModBlocks.COPPER_MACHINE.get(), cubeAll(ModBlocks.COPPER_MACHINE.get()));
        simpleBlockWithItem(ModBlocks.BRASS_MACHINE.get(), cubeAll(ModBlocks.BRASS_MACHINE.get()));
        simpleBlockWithItem(ModBlocks.REDSTONE_MACHINE.get(), cubeAll(ModBlocks.REDSTONE_MACHINE.get()));
    }
}
