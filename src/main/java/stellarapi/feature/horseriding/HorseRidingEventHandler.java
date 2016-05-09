package stellarapi.feature.horseriding;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.event.interact.ApplyOpticalEntityEvent;

public class HorseRidingEventHandler {
	
	@SubscribeEvent
	public void updateScopeForHorseRider(UpdateScopeEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			
			if(player.ridingEntity instanceof EntityHorse)
				event.setScope(new ScopeHorseRiding(event.getScope()));
		}
	}
	
	@SubscribeEvent
	public void applyOpticsOnHorse(ApplyOpticalEntityEvent event) {
		if(event.getRidingEntity() instanceof EntityHorse)
			event.setIsViewScope(true);
	}
	
}
