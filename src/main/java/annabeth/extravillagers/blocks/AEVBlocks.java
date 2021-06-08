package annabeth.extravillagers.blocks;

import annabeth.extravillagers.VillagerMain;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

public class AEVBlocks {
	public static Block SAWMILL;
	public static Block CERAMICS_TABLE;
	
	private static boolean areBlocksCreated = false;
	
	public static void createBlocks() {
		if (areBlocksCreated) return;
		areBlocksCreated = true;
		
		SAWMILL = new SawmillBlock().setRegistryName(VillagerMain.MODID, "sawmill");
		CERAMICS_TABLE = new Block(AbstractBlock.Properties.of(Material.STONE)).setRegistryName(VillagerMain.MODID, "ceramics_table");
	}
	
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		createBlocks();
		
		reg.register(SAWMILL);
		reg.register(CERAMICS_TABLE);
	}
	
	public static void registerBlockItems(IForgeRegistry<Item> reg) {
		createBlocks();
		
		registerBlockItem(reg, SAWMILL, ItemGroup.TAB_DECORATIONS);
		registerBlockItem(reg, CERAMICS_TABLE, ItemGroup.TAB_DECORATIONS);
	}
	
	private static void registerBlockItem(IForgeRegistry<Item> reg, Block block) {
		registerBlockItem(reg, block, ItemGroup.TAB_BUILDING_BLOCKS);
	}
	
	private static void registerBlockItem(IForgeRegistry<Item> reg, Block block, ItemGroup group) {
		reg.register(new BlockItem(block, new Item.Properties().tab(group)).setRegistryName(block.getRegistryName()));
	}
}