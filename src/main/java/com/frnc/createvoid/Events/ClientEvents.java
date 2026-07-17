package com.frnc.createvoid.Events;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.block.ModBlocks;
import com.frnc.createvoid.fluid.ModFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CreateVoid.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 设置流体渲染为不透明（粘稠质感）
            ItemBlockRenderTypes.setRenderLayer(ModFluids.KELP_GEL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_KELP_GEL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.KELP_GEL_BLOCK.get(), RenderType.solid());
        });
    }
}
