package net.name.culinarymod.item;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.name.culinarymod.CulinaryMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CulinaryMod.MOD_ID);

    //seed oil item
    public static final RegistryObject<Item> SEEDOIL = ITEMS.register("seedoil",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    //knife item
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife",
            () -> new SwordItem(Tiers.IRON,10,5f,
                    new Item.Properties().tab(ModCreativeModeTab.CULINARY_TAB).stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
