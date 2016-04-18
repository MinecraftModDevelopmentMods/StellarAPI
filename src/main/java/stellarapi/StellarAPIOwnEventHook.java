package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import stellarapi.api.IViewScope;
import stellarapi.api.event.ResetScopeEvent;

public class StellarAPIOwnEventHook {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onResetScope(ResetScopeEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack usingItem = player.getItemInUse();
			if(usingItem != null && usingItem.getItem() instanceof IViewScope)
				event.setScope((IViewScope) usingItem.getItem());
		}
	}

}
