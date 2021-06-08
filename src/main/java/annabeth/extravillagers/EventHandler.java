package annabeth.extravillagers;

import java.util.List;

import annabeth.extravillagers.villagers.VillagerRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = VillagerMain.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void villagerTrades(VillagerTradesEvent e) {
		Int2ObjectMap<List<ITrade>> trades = e.getTrades();
		VillagerProfession prof = e.getType();
		VillagerRegistry.registerTrades(trades, prof);
	}
}