package stellarapi;

import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPIReferences;

public class SAPIForgeEventHook {
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if (!SAPIReferences.getSleepWakeManager().isEnabled() || event.getEntityPlayer().world.isRemote) {
			return;
		}

		if (event.getResultStatus() == null || event.getResultStatus() == SleepResult.OK
				|| event.getResultStatus() == SleepResult.NOT_POSSIBLE_NOW) {
			World worldObj = event.getEntityPlayer().world;

			event.setResult(
					SAPIReferences.getSleepWakeManager().getSleepPossibility(worldObj, event.getResultStatus()));
		}

		if (event.getResultStatus() == SleepResult.OK) {
			event.getEntityPlayer().world.updateAllPlayersSleepingFlag();
			event.setResult((SleepResult) null);
		}
	}


	@SubscribeEvent
	public void onSyncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(SAPIReferences.MODID))
			StellarAPI.INSTANCE.getCfgManager().syncFromGUI();
	}
}