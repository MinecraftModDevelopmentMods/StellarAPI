package stellarapi;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.event.FOVEvent;
import stellarapi.api.event.QEEvent;
import stellarapi.api.interact.IFilter;
import stellarapi.api.interact.IScope;

@Mod.EventBusSubscriber(modid = SAPIReferences.MODID)
public class SAPICommonEventHook {
	@SubscribeEvent
	public static void onDecideFOV(FOVEvent event) {
		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack active = viewer.getActiveItemStack();
			if(active != null) {
				IScope scope = active.getCapability(SAPICapabilities.SCOPE_CAPABILITY, null);
				if(scope != null)
					event.setFOV(scope.transformFOV(event.getFOV()));
			}
		}
	}

	@SubscribeEvent
	public static void onDecideQE(QEEvent event) {
		if(event.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase viewer = (EntityLivingBase) event.getEntity();
			ItemStack active = viewer.getActiveItemStack();
			if(active != null) {
				IFilter filter = active.getCapability(SAPICapabilities.FILTER_CAPABILITY, null);
				if(filter != null)
					event.setQE(filter.transformQE(event.getWavelength(), event.getQE()));
			}
		}
	}


	@SubscribeEvent
	public static void onSleepInBed(PlayerSleepInBedEvent event) {
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
	public static void onSyncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(SAPIReferences.MODID))
			StellarAPI.INSTANCE.getCfgManager().syncFromGUI();
	}
}