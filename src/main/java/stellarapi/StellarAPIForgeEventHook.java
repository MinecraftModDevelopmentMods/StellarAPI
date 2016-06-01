package stellarapi;

import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.interact.ApplyOpticalItemEvent;
import stellarapi.api.event.world.ClientWorldEvent;
import stellarapi.api.event.world.ServerWorldEvent;
import stellarapi.api.helper.LivingItemAccessHelper;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.reference.OpticalViewerEventCallback;
import stellarapi.reference.PerServerManager;

public class StellarAPIForgeEventHook {

	@SubscribeEvent
	public void onStartUsingItem(LivingEntityUseItemEvent.Start event) {
		IOpticalViewer optics = event.getEntity().getCapability(StellarAPICapabilities.VIEWER_CAPABILITY, EnumFacing.DOWN);

		if(optics instanceof OpticalViewerEventCallback) {
			ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(event.getEntityLiving(), event.getItem());
			StellarAPIReference.getEventBus().post(applyEvent);

			ItemStack previous = LivingItemAccessHelper.getUsingItem(event.getEntityLiving());
			LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), event.getItem());

			OpticalViewerEventCallback callback = (OpticalViewerEventCallback) optics;
			
			if(applyEvent.isViewScope())
				callback.updateScope();
			if(applyEvent.isOpticalFilter())
				callback.updateFilter();

			LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), previous);
		}
	}
	
	@SubscribeEvent
	public void onStopUsingItem(LivingEntityUseItemEvent.Stop event) {
		this.onEndItemUse(event);
	}
	
	@SubscribeEvent
	public void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
		this.onEndItemUse(event);
	}
	
	private void onEndItemUse(LivingEntityUseItemEvent event) {
		IOpticalViewer optics = event.getEntity().getCapability(StellarAPICapabilities.VIEWER_CAPABILITY, EnumFacing.DOWN);
		
		if(optics instanceof OpticalViewerEventCallback) {
			ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(event.getEntityLiving(), event.getItem());
			StellarAPIReference.getEventBus().post(applyEvent);

			ItemStack previous = LivingItemAccessHelper.getUsingItem(event.getEntityLiving());
			LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), null);

			OpticalViewerEventCallback callback = (OpticalViewerEventCallback) optics;
			
			if(applyEvent.isViewScope())
				callback.updateScope();
			if(applyEvent.isOpticalFilter())
				callback.updateFilter();

			LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), previous);
		}
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		if(event.getWorld().isRemote)
			StellarAPIReference.getEventBus().post(new ClientWorldEvent.Load(event.getWorld(), StellarAPI.proxy.getLoadingProgress()));
		else {
			MinecraftServer server = event.getWorld().getMinecraftServer();
			if(!PerServerManager.isInitiated(server)) {
				StellarAPIReference.getEventBus().post(new ServerWorldEvent.Initial(server, server.getEntityWorld()));
				PerServerManager.initiatePerServerManager(server);
			}
			
			StellarAPIReference.getEventBus().post(new ServerWorldEvent.Load(server, event.getWorld()));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldUnload(WorldEvent.Unload event) {
		if(event.getWorld().isRemote)
			StellarAPIReference.getEventBus().post(new ClientWorldEvent.Unload(event.getWorld(), StellarAPI.proxy.getLoadingProgress()));
		else {
			MinecraftServer server = event.getWorld().getMinecraftServer();
			StellarAPIReference.getEventBus().post(new ServerWorldEvent.Unload(server, event.getWorld()));
		}
	}
	
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if(!StellarAPIReference.getSleepWakeManager().isEnabled() || event.getEntityPlayer().worldObj.isRemote) {
			return;
		}
		
		if(event.getResultStatus() == null || event.getResultStatus() == SleepResult.OK || event.getResultStatus() == SleepResult.NOT_POSSIBLE_NOW) {
			World worldObj = event.getEntityPlayer().worldObj;
			
			event.setResult(StellarAPIReference.getSleepWakeManager()
					.getSleepPossibility(worldObj, event.getResultStatus()));
		}
		
		if(event.getResultStatus() == SleepResult.OK)
		{
	        event.getEntityPlayer().worldObj.updateAllPlayersSleepingFlag();
	        event.setResult((SleepResult)null);
		}
	}
}
