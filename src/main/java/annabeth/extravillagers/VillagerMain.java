package annabeth.extravillagers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import annabeth.extravillagers.blocks.AEVBlocks;
import annabeth.extravillagers.villagers.VillagerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(VillagerMain.MODID)
public class VillagerMain {
	public static final String MODID = "annabethsextravillagers";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public VillagerMain() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void setup(final FMLCommonSetupEvent e) {
		AEVBlocks.createBlocks();
		e.enqueueWork(VillagerRegistry::init);
	}
	
	public void clientSetup(final FMLClientSetupEvent e) {
		RenderTypeLookup.setRenderLayer(AEVBlocks.SAWMILL, RenderType.cutout());
	}
}