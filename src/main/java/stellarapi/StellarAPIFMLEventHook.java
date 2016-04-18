package stellarapi;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StellarAPIFMLEventHook {
	@SubscribeEvent
	public void onSyncConfig(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID == StellarAPI.modid)
			StellarAPI.instance.getCfgManager().syncFromGUI();
	}
}
