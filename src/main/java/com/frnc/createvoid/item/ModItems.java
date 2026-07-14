package com.frnc.createvoid.item;

import com.frnc.createvoid.CreateVoid;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateVoid.MOD_ID);

    public static final RegistryObject<Item> IRON_MECHANISM =
            ITEMS.register("iron_mechanism", ()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> INCOMPLETE_BRASS_MACHINE =
            ITEMS.register("incomplete_brass_machine", ()->new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
