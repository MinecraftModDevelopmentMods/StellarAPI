package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import stellarapi.api.PerWorldManager;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.interact.ApplyOpticalItemEvent;
import stellarapi.api.helper.PlayerItemAccessHelper;

public class StellarAPIForgeEventHook {
	
	@SubscribeEvent
	public void onStartUsingItem(PlayerUseItemEvent.Start event) {
		ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(event.entityPlayer, event.item);
		StellarAPIReference.getEventBus().post(applyEvent);
		
		ItemStack previous = PlayerItemAccessHelper.getUsingItem(event.entityPlayer);
		PlayerItemAccessHelper.setUsingItem(event.entityPlayer, event.item);
		
		if(applyEvent.isViewScope())
			StellarAPIReference.updateScope(event.entityPlayer);
		if(applyEvent.isOpticalFilter())
			StellarAPIReference.updateFilter(event.entityPlayer);
		
		PlayerItemAccessHelper.setUsingItem(event.entityPlayer, previous);
	}
	
	@SubscribeEvent
	public void onStopUsingItem(PlayerUseItemEvent.Stop event) {
		this.onEndItemUse(event);
	}
	
	@SubscribeEvent
	public void onFinishUsingItem(PlayerUseItemEvent.Finish event) {
		this.onEndItemUse(event);
	}
	
	private void onEndItemUse(PlayerUseItemEvent event) {
		ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(event.entityPlayer, event.item);
		StellarAPIReference.getEventBus().post(applyEvent);
		
		ItemStack previous = PlayerItemAccessHelper.getUsingItem(event.entityPlayer);
		PlayerItemAccessHelper.setUsingItem(event.entityPlayer, null);
		
		if(applyEvent.isViewScope())
			StellarAPIReference.updateScope(event.entityPlayer);
		if(applyEvent.isOpticalFilter())
			StellarAPIReference.updateFilter(event.entityPlayer);
		
		PlayerItemAccessHelper.setUsingItem(event.entityPlayer, previous);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		PerWorldManager.initiatePerWorldManager(event.world);
	}
	
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if(!StellarAPIReference.getSleepWakeManager().isEnabled() || event.entityPlayer.worldObj.isRemote) {
			return;
		}
		
		if(event.result == null || event.result == EnumStatus.OK || event.result == EnumStatus.NOT_POSSIBLE_NOW) {
			World worldObj = event.entityPlayer.worldObj;
			
			event.result = StellarAPIReference.getSleepWakeManager()
					.getSleepPossibility(worldObj, event.result);
		}
		
		if(event.result == EnumStatus.OK)
		{
	        event.entityPlayer.worldObj.updateAllPlayersSleepingFlag();
	        event.result = null;
		}
	}
}
