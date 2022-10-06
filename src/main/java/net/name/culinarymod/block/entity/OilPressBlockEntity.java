package net.name.culinarymod.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.name.culinarymod.item.ModItems;
import net.name.culinarymod.screen.OilPressMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OilPressBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler  itemhandler = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress =0;
    private int maxProgress = 100;


    public OilPressBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OIL_PRESS.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> OilPressBlockEntity.this.progress;
                    case 1 -> OilPressBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> OilPressBlockEntity.this.progress = value;
                    case 1 -> OilPressBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Oil Press");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new OilPressMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemhandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemhandler.serializeNBT());
        nbt.putInt("oil_press.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemhandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("oil_press.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemhandler.getSlots());
        for (int i = 0; i < itemhandler.getSlots(); i++) {
            inventory.setItem(i, itemhandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, OilPressBlockEntity pEntity) {
        if(level.isClientSide()){
            return;
        }

        if(hasRecipe(pEntity)){
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress){
                craftItem(pEntity);
            }
        } else{
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(OilPressBlockEntity pEntity) {

        if(hasRecipe(pEntity)){
            pEntity.itemhandler.extractItem(1,1,false);
            pEntity.itemhandler.setStackInSlot(2,new ItemStack(ModItems.SEEDOIL.get(),
                pEntity.itemhandler.getStackInSlot(2).getCount() + 1));

            pEntity.resetProgress();
        }

    }

    private static boolean hasRecipe(OilPressBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemhandler.getSlots());
        for (int i = 0; i < entity.itemhandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemhandler.getStackInSlot(i));
        }

        boolean hasSeedInFirstSlot = entity.itemhandler.getStackInSlot(1).getItem() == Items.WHEAT_SEEDS;

        return hasSeedInFirstSlot && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, new ItemStack(ModItems.SEEDOIL.get(),1));


    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(2).getItem() == itemStack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
     return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }
}
