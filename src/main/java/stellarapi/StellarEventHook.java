package stellarapi;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.StellarAPIReference;

public class StellarEventHook {
	
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if(!StellarAPI.proxy.wakeManager.isEnabled() || event.entityPlayer.worldObj.isRemote) {
			return;
		}

		if(event.result == null || event.result == EnumStatus.OK || event.result == EnumStatus.NOT_POSSIBLE_NOW) {
			World worldObj = event.entityPlayer.worldObj;
			ICelestialCoordinate coordinate = StellarAPIReference.getCoordinate(worldObj);
			CelestialLightSources lightSources = StellarAPIReference.getLightSources(worldObj);
			
			if(coordinate == null || lightSources == null)
				return;

			if (!StellarAPI.proxy.wakeManager.canSkipTime(worldObj, lightSources, coordinate, worldObj.getWorldTime()))
				event.result = EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
		}
	}
	
}
