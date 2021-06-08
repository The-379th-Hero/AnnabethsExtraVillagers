package annabeth.extravillagers.container;

import java.util.List;

import com.google.common.collect.Lists;

import annabeth.coremod.crafting.RecipeVars;
import annabeth.coremod.crafting.SawmillRecipe;
import annabeth.extravillagers.blocks.AEVBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class SawmillContainer extends Container {
	public static ContainerType<SawmillContainer> SAWMILL_CONTAINER = IForgeContainerType.create(SawmillContainer::new);
	
	private final IWorldPosCallable access;
	private final IntReferenceHolder selectedRecipeIndex = IntReferenceHolder.standalone();
	private final World level;
	private List<SawmillRecipe> recipes = Lists.newArrayList();
	private ItemStack input = ItemStack.EMPTY;
	private long lastSoundTime;
	final Slot inputSlot;
	final Slot resultSlot;
	private Runnable slotUpdateListener = () -> {
	};
	public final IInventory container = new Inventory(1) {
		@Override
		public void setChanged() {
			super.setChanged();
			SawmillContainer.this.slotsChanged(this);
			SawmillContainer.this.slotUpdateListener.run();
		}
	};
	private final CraftResultInventory resultContainer = new CraftResultInventory();
	
	public SawmillContainer(int id, PlayerInventory inv) {
		this (id, inv, IWorldPosCallable.NULL);
	}
	
	public SawmillContainer(int id, PlayerInventory inventory, PacketBuffer data) {
		this(id, inventory, IWorldPosCallable.create(inventory.player.level, data.readBlockPos()));
	}
	
	public SawmillContainer(int id, PlayerInventory inv, IWorldPosCallable iwpc) {
		super(SAWMILL_CONTAINER, id);
		this.access = iwpc;
		this.level = inv.player.level;
		this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
		this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
			public boolean mayPlace(ItemStack stack) {
				return false;
			}
			
			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
				stack.onCraftedBy(player.level, player, stack.getCount());
				SawmillContainer.this.resultContainer.awardUsedRecipes(player);
				ItemStack stack1 = SawmillContainer.this.inputSlot.remove(1);
				if (!stack1.isEmpty()) {
					SawmillContainer.this.setupResultSlot();
				}
				
				
				iwpc.execute((world, pos) -> {
					long l = world.getGameTime();
					if (SawmillContainer.this.lastSoundTime != l) {
						world.playSound((PlayerEntity) null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
						SawmillContainer.this.lastSoundTime = l;
					}
				});
				
				return super.onTake(player, stack);
			}
		});
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
		}
		
		this.addDataSlot(this.selectedRecipeIndex);
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getSelectedRecipeIndex() {
		return this.selectedRecipeIndex.get();
	}
	
	@OnlyIn(Dist.CLIENT)
	public List<SawmillRecipe> getRecipes() {
		return this.recipes;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getNumRecipes() {
		return this.recipes.size();
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean hasInputItem() {
		return this.inputSlot.hasItem() && !this.recipes.isEmpty();
	}
	
	@Override
	public boolean clickMenuButton(PlayerEntity player, int index) {
		if (this.isValidRecipeIndex(index)) {
			this.selectedRecipeIndex.set(index);
			this.setupResultSlot();
		}
		
		return true;
	}
	
	private boolean isValidRecipeIndex(int index) {
		return index >= 0 && index < this.recipes.size();
	}
	
	@Override
	public void slotsChanged(IInventory inv) {
		ItemStack itemstack = this.inputSlot.getItem();
		if (itemstack.getItem() != this.input.getItem()) {
			this.input = itemstack.copy();
			this.setupRecipeList(inv, itemstack);
		}
	}
	
	private void setupRecipeList(IInventory inv, ItemStack stack) {
		this.recipes.clear();
		this.selectedRecipeIndex.set(-1);
		this.resultSlot.set(ItemStack.EMPTY);
		if (!stack.isEmpty()) {
			this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeVars.SAWMILL_RECIPE, inv, this.level);
		}
	}
	
	private void setupResultSlot() {
		if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
			SawmillRecipe recipe = this.recipes.get(this.selectedRecipeIndex.get());
			this.resultContainer.setRecipeUsed(recipe);
			this.resultSlot.set(recipe.assemble(this.container));
		} else {
			this.resultSlot.set(ItemStack.EMPTY);
		}
		
		this.broadcastChanges();
	}
	
	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(this.access, player, AEVBlocks.SAWMILL);
	}
	
	@Override
	public ContainerType<?> getType() {
		return SAWMILL_CONTAINER;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void registerUpdateListener(Runnable r) {
		this.slotUpdateListener = r;
	}
	
	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return slot.container != this.resultContainer && super.canTakeItemForPickAll(stack, slot);
	}
	
	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int slotID) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotID);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			Item item = stack1.getItem();
			stack = stack1.copy();
			
			if (slotID == 1) {
				item.onCraftedBy(stack1, player.level, player);
				if (!this.moveItemStackTo(stack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onQuickCraft(stack1, stack);
			} else if (slotID == 0) {
				if (!this.moveItemStackTo(stack1, 2, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.level.getRecipeManager().getRecipeFor(RecipeVars.SAWMILL_RECIPE, new Inventory(stack1), this.level).isPresent()) {
				if (!this.moveItemStackTo(stack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotID >= 2 && slotID < 29) {
				if (!this.moveItemStackTo(stack1, 29, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotID >= 29 && slotID < 38 && !this.moveItemStackTo(stack1, 2, 29, false)) {
				return ItemStack.EMPTY;
			}
			
			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			}
			
			slot.setChanged();
			if (stack1.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, stack1);
			this.broadcastChanges();
		}
		
		return stack;
	}
	
	@Override
	public void removed(PlayerEntity player) {
		super.removed(player);
		this.resultContainer.removeItemNoUpdate(1);
		this.access.execute((world, pos) -> {
			this.clearContainer(player, player.level, this.container);
		});
	}
}
