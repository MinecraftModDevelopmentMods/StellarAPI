package stellarapi;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPIReferences;

public class StellarAPIFMLEventHook {
	@SubscribeEvent
	public void onSyncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(SAPIReferences.MODID))
			StellarAPI.INSTANCE.getCfgManager().syncFromGUI();
	}
}
