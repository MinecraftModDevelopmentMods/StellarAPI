package stellarapi;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StellarAPIFMLEventHook {
	@SubscribeEvent
	public void onSyncConfig(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(StellarAPI.modid))
			StellarAPI.instance.getCfgManager().syncFromGUI();
	}
}
