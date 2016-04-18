package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import stellarapi.api.IViewScope;
import stellarapi.api.StellarAPIReference;

public class StellarAPIClientForgeEventHook {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUpdateFOV(FOVUpdateEvent event) {
		IViewScope scope = StellarAPIReference.getScope(event.entity);
		if(scope.forceChange())
			event.newfov = event.fov * (float)scope.getMP();
		else event.newfov *= (float)scope.getMP();
	}
	
	public void onRenderWorld() {
		
	}
}
