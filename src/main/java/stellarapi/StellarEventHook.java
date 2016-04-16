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
