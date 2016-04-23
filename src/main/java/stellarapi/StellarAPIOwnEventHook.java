package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import stellarapi.api.event.UpdateFilterEvent;
import stellarapi.api.event.UpdateScopeEvent;
import stellarapi.api.helper.PlayerItemAccessHelper;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;

public class StellarAPIOwnEventHook {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateScope(UpdateScopeEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			Object[] params = event.getAdditionalParams();
			
			ItemStack itemToCheck;
			if(params.length < 1 || !(params[0] instanceof ItemStack))
				itemToCheck = PlayerItemAccessHelper.getUsingItem(player);
			else itemToCheck = (ItemStack) params[0];
			
			if(itemToCheck != null && itemToCheck.getItem() instanceof IViewScope)
				event.setScope((IViewScope) itemToCheck.getItem());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUpdateFilter(UpdateFilterEvent event) {
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			Object[] params = event.getAdditionalParams();
			
			ItemStack itemToCheck;
			if(params.length < 1 || !(params[0] instanceof ItemStack))
				itemToCheck = PlayerItemAccessHelper.getUsingItem(player);
			else itemToCheck = (ItemStack) params[0];
			
			if(itemToCheck != null && itemToCheck.getItem() instanceof IOpticalFilter)
				event.setFilter((IOpticalFilter) itemToCheck.getItem());
		}
	}

	
}
