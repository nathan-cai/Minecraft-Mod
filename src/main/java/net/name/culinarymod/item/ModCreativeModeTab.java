package net.name.culinarymod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.name.culinarymod.block.ModBlocks;

public class ModCreativeModeTab {
    public static final CreativeModeTab CULINARY_TAB = new CreativeModeTab("culinarytab") {
        @Override
        public ItemStack makeIcon() {
            return Items.COOKED_BEEF.getDefaultInstance();
//            return new ItemStack(ModBlocks.COUNTERTOP_BLOCK.get());
        }
    };
}
