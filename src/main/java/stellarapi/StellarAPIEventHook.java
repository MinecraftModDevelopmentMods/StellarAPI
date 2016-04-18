package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.WorldEvent;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.IViewScope;
import stellarapi.api.PerEntityManager;
import stellarapi.api.PerWorldManager;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.celestials.CelestialLightSources;

public class StellarAPIEventHook {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		PerWorldManager.initiatePerWorldManager(event.world);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLoad(EntityEvent.EntityConstructing event) {
		StellarAPIReference.resetScope(event.entity);
	}
	
	@SubscribeEvent
	public void onUpdateFOV(FOVUpdateEvent event) {
		IViewScope scope = StellarAPIReference.getScope(event.entity);
		if(scope.forceChange())
			event.newfov = event.fov * (float)scope.getMP();
		event.newfov = event.newfov * (float)scope.getMP();
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
	}
}
