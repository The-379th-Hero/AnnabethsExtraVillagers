package annabeth.extravillagers;

import annabeth.extravillagers.blocks.AEVBlocks;
import annabeth.extravillagers.container.SawmillContainer;
import annabeth.extravillagers.villagers.VillagerRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = VillagerMain.MODID)
public class RegistryHandler {
	
	public static final ResourceLocation INTERACT_WITH_SAWMILL = new ResourceLocation(VillagerMain.MODID, "interact_with_sawmill");
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e) {
		Registry.register(Registry.CUSTOM_STAT, "annabethsextravillagers:interact_with_sawmill", INTERACT_WITH_SAWMILL);
		AEVBlocks.registerBlocks(e.getRegistry());
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e) {
		AEVBlocks.registerBlockItems(e.getRegistry());
	}
	
	@SubscribeEvent
	public static void registerVillagerProfessions(RegistryEvent.Register<VillagerProfession> e) {
		e.getRegistry().register(VillagerRegistry.CARPENTER);
		e.getRegistry().register(VillagerRegistry.POTTER);
		e.getRegistry().register(VillagerRegistry.MUSICIAN);
	}
	
	@SubscribeEvent
	public static void registerPOITypes(RegistryEvent.Register<PointOfInterestType> e) {
		e.getRegistry().register(VillagerRegistry.SAWMILL);
		e.getRegistry().register(VillagerRegistry.CERAMICS_TABLE);
		e.getRegistry().register(VillagerRegistry.JUKEBOX);
	}
	
	@SubscribeEvent
	public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> e) {
		e.getRegistry().register(SawmillContainer.SAWMILL_CONTAINER.setRegistryName(VillagerMain.MODID, "sawmill_container"));
	}
}