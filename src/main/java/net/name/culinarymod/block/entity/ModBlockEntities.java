package net.name.culinarymod.block.entity;

import io.netty.resolver.DefaultHostsFileEntriesResolver;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.name.culinarymod.CulinaryMod;
import net.name.culinarymod.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
           DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CulinaryMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<OilPressBlockEntity>> OIL_PRESS =
            BLOCK_ENTITIES.register("oil_press", () ->
                    BlockEntityType.Builder.of(OilPressBlockEntity::new,
                            ModBlocks.OIL_PRESS.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);

    }
}
