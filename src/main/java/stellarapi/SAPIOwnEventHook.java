package stellarapi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.event.interact.CheckEntityOpticalViewerEvent;

public class SAPIOwnEventHook {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkOpticalEntity(CheckEntityOpticalViewerEvent event) {
		// TODO better implementation & logic of checking for the optical entities.
		event.setIsOpticalEntity(event.getEntity() instanceof EntityPlayer);
	}
}
