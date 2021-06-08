package annabeth.extravillagers;

import annabeth.extravillagers.client.screen.SawmillScreen;
import annabeth.extravillagers.container.SawmillContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = VillagerMain.MODID, value = Dist.CLIENT)
public class ClientRegistryHandler {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent e) {
		ScreenManager.register(SawmillContainer.SAWMILL_CONTAINER, SawmillScreen::new);
	}
}