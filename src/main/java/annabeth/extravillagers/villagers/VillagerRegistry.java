package annabeth.extravillagers.villagers;

import java.util.List;
import java.util.Random;

import annabeth.coremod.util.AnnabethTags;
import annabeth.coremod.util.VillagerHelper;
import annabeth.extravillagers.VillagerMain;
import annabeth.extravillagers.blocks.AEVBlocks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.structure.DesertVillagePools;
import net.minecraft.world.gen.feature.structure.PlainsVillagePools;
import net.minecraft.world.gen.feature.structure.SavannaVillagePools;
import net.minecraft.world.gen.feature.structure.SnowyVillagePools;
import net.minecraft.world.gen.feature.structure.TaigaVillagePools;
import net.minecraftforge.common.Tags;

public class VillagerRegistry {
	public static PointOfInterestType SAWMILL = VillagerHelper.makePOI(new ResourceLocation(VillagerMain.MODID, "sawmill"), AEVBlocks.SAWMILL).setRegistryName(VillagerMain.MODID, "sawmill");
	public static PointOfInterestType CERAMICS_TABLE = VillagerHelper.makePOI(new ResourceLocation(VillagerMain.MODID), AEVBlocks.CERAMICS_TABLE).setRegistryName(VillagerMain.MODID, "ceramics_table");
	public static PointOfInterestType JUKEBOX = VillagerHelper.makePOI(new ResourceLocation(VillagerMain.MODID, "jukebox"), Blocks.JUKEBOX).setRegistryName(VillagerMain.MODID, "jukebox");
	
	public static VillagerProfession CARPENTER = VillagerHelper.makeProfession(new ResourceLocation(VillagerMain.MODID, "carpenter"), SAWMILL, SoundEvents.VILLAGER_WORK_MASON).setRegistryName(VillagerMain.MODID, "carpenter");
	public static VillagerProfession POTTER = VillagerHelper.makeProfession(new ResourceLocation(VillagerMain.MODID, "potter"), CERAMICS_TABLE, SoundEvents.GRAVEL_BREAK).setRegistryName(VillagerMain.MODID, "potter");
	public static VillagerProfession MUSICIAN = VillagerHelper.makeProfession(new ResourceLocation(VillagerMain.MODID, "musician"), JUKEBOX, SoundEvents.NOTE_BLOCK_FLUTE).setRegistryName(VillagerMain.MODID, "musician");
	
	private static boolean isInit = false;
	
	public static void init() {
		if (isInit) {
			return;
		}
		
		isInit = true;
		

		DesertVillagePools.bootstrap();
		PlainsVillagePools.bootstrap();
		SnowyVillagePools.bootstrap();
		SavannaVillagePools.bootstrap();
		TaigaVillagePools.bootstrap();
		
		for (String biome : new String[] {"desert", "plains", "snowy", "savanna", "taiga"}) {
			for (String building : new String[] {"woodshop", "potters", "music"}) {
				VillagerHelper.addVillagerHouse(new ResourceLocation("village/" + biome + "/houses"), new ResourceLocation(VillagerMain.MODID, "village/" + biome + "/houses/" + biome + "_" + building));
			}
		}
		
		VillagerHelper.setHOTVGifts(CARPENTER);
		VillagerHelper.setHOTVGifts(POTTER);
		VillagerHelper.setHOTVGifts(MUSICIAN);
	}
	
	public static void registerTrades(Int2ObjectMap<List<ITrade>> trades, VillagerProfession prof) {
		switch (prof.getRegistryName().getPath()) {
			case "carpenter":
				trades.get(1).add(new SellingTradeWithTag(ItemTags.LOGS_THAT_BURN, 1, 3, 1, 1, 8, 3));
				trades.get(1).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_PLANKS, 1, 3, 16, 24, 8, 3));
				trades.get(2).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_SLABS, 2, 4, 8, 16, 8, 3));
				trades.get(2).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_STAIRS, 2, 4, 8, 16, 8, 3));
				trades.get(3).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_FENCES, 2, 4, 4, 8, 8, 3));
				trades.get(3).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_FENCE_GATES, 2, 4, 4, 8, 8, 3));
				trades.get(4).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_DOORS, 2, 4, 2, 4, 8, 3));
				trades.get(4).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.OVERWORLD_TRAPDOORS, 2, 4, 2, 4, 8, 3));
				trades.get(5).add(new BuyingTradeWithTag(Tags.Items.CHESTS_WOODEN, 1, 3, 1, 4, 8, 3));
				break;
			case "potter":
				trades.get(1).add(new SellingTrade(Items.CLAY, 2, 4, 1, 2, 8, 3));
				trades.get(1).add(new BuyingTrade(Items.BRICK, 1, 3, 4, 10, 8, 3));
				trades.get(1).add(new BuyingTrade(Items.TERRACOTTA, 1, 3, 1, 4, 8, 3));
				trades.get(2).add(new BuyingTrade(Items.BRICKS, 1, 3, 1, 4, 8, 3));
				trades.get(2).add(new BuyingTrade(Items.BRICK_STAIRS, 1, 3, 1, 4, 8, 3));
				trades.get(2).add(new BuyingTrade(Items.BRICK_SLAB, 1, 3, 1, 4, 8, 3));
				trades.get(2).add(new BuyingTrade(Items.BRICK_WALL, 1, 3, 1, 4, 8, 3));
				trades.get(3).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.COLORED_TERRACOTTA, 2, 5, 2, 3, 8, 3));
				trades.get(4).add(new BuyingTradeWithTag(AnnabethTags.ItemTagList.GLAZED_TERRACOTTA, 2, 5, 1, 2, 8, 3));
				trades.get(5).add(new BuyingTrade(Items.FLOWER_POT, 4, 5, 1, 1, 8, 3));
				break;
			case "musician":
				trades.get(1).add(new SellingTrade(Items.DIAMOND, 1, 3, 1, 1, 8, 3));
				trades.get(1).add(new SellingTradeWithTag(ItemTags.FLOWERS, 5, 7, 1, 1, 8, 3));
				trades.get(2).add(new BuyingTradeWithTag(ItemTags.CREEPER_DROP_MUSIC_DISCS, 8, 13, 1, 1, 8, 7));
				trades.get(3).add(new BuyingTradeWithTag(ItemTags.CREEPER_DROP_MUSIC_DISCS, 8, 13, 1, 1, 8, 7));
				trades.get(4).add(new BuyingTradeWithTag(ItemTags.MUSIC_DISCS, 9, 17, 1, 1, 8, 7));
				trades.get(5).add(new BuyingTradeWithTag(ItemTags.MUSIC_DISCS, 9, 17, 1, 1, 8, 7));
				break;
		}
	}
	
	public static class BuyingTrade implements ITrade {
		private final Item result;
		private final int priceMin, priceMax, resultMin, resultMax, maxUses, xp;
		
		public BuyingTrade(Item item, int pMin, int pMax, int rMin, int rMax, int uses, int xp) {
			this.result = item;
			this.priceMin = pMin;
			this.priceMax = pMax;
			this.resultMin = rMin;
			this.resultMax = rMax;
			this.maxUses = uses;
			this.xp = xp;
		}
		
		@Override
		public MerchantOffer getOffer(Entity trader, Random random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, priceMin + random.nextInt(priceMax-priceMin+1)), new ItemStack(result, resultMin + random.nextInt(resultMax-resultMin+1)), maxUses, xp, 0.05f);
		}
	}
	
	public static class BuyingTradeWithTag implements ITrade {
		private final ITag<Item> result;
		private final int priceMin, priceMax, resultMin, resultMax, maxUses, xp;
		
		public BuyingTradeWithTag(ITag<Item> tag, int pMin, int pMax, int rMin, int rMax, int uses, int xp) {
			this.result = tag;
			this.priceMin = pMin;
			this.priceMax = pMax;
			this.resultMin = rMin;
			this.resultMax = rMax;
			this.maxUses = uses;
			this.xp = xp;
		}
		
		@Override
		public MerchantOffer getOffer(Entity trader, Random random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, priceMin + random.nextInt(priceMax-priceMin+1)), new ItemStack(getRandomItemByTag(result, random), resultMin + random.nextInt(resultMax-resultMin+1)), maxUses, xp, 0.05f);
		}
	}
	
	public static class SellingTrade implements ITrade {
		private final Item cost;
		private final int priceMin, priceMax, resultMin, resultMax, maxUses, xp;
		
		public SellingTrade(Item item, int pMin, int pMax, int rMin, int rMax, int uses, int xp) {
			this.cost = item;
			this.priceMin = pMin;
			this.priceMax = pMax;
			this.resultMin = rMin;
			this.resultMax = rMax;
			this.maxUses = uses;
			this.xp = xp;
		}
		
		@Override
		public MerchantOffer getOffer(Entity trader, Random random) {
			return new MerchantOffer(new ItemStack(cost, priceMin + random.nextInt(priceMax-priceMin+1)), new ItemStack(Items.EMERALD, resultMin + random.nextInt(resultMax-resultMin+1)), maxUses, xp, 0.05f);
		}
	}
	
	public static class SellingTradeWithTag implements ITrade {
		private final ITag<Item> cost;
		private final int priceMin, priceMax, resultMin, resultMax, maxUses, xp;
		
		public SellingTradeWithTag(ITag<Item> tag, int pMin, int pMax, int rMin, int rMax, int uses, int xp) {
			this.cost = tag;
			this.priceMin = pMin;
			this.priceMax = pMax;
			this.resultMin = rMin;
			this.resultMax = rMax;
			this.maxUses = uses;
			this.xp = xp;
		}
		
		@Override
		public MerchantOffer getOffer(Entity trader, Random random) {
			return new MerchantOffer(new ItemStack(getRandomItemByTag(cost, random), priceMin + random.nextInt(priceMax-priceMin+1)), new ItemStack(Items.EMERALD, resultMin + random.nextInt(resultMax-resultMin+1)), maxUses, xp, 0.05f);
		}
	}
	
	public static Item getRandomItemByTag(ITag<Item> tag, Random random) {
		List<Item> items = tag.getValues();
		
		return items.get(random.nextInt(items.size()));
	}
}