package net.name.culinarymod.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.name.culinarymod.CulinaryMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            CulinaryMod.MOD_ID);

    // seed oil item
    public static final RegistryObject<Item> SEEDOIL = ITEMS.register("seedoil",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BEEFSLAB = ITEMS.register("beefslab",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.CULINARY_TAB).food(ModFoods.BEEFSLAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
