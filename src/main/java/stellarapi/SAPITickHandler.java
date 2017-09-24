package stellarapi;

import java.lang.reflect.Method;

import com.google.common.base.Throwables;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.SAPIReferences;
import stellarapi.api.helper.LivingItemAccessHelper;

public class SAPITickHandler {

	private Method wakeAllPlayers = null;
	
	private void wakeAllPlayers(WorldServer world) {
		if(this.wakeAllPlayers == null) {
			this.wakeAllPlayers = ReflectionHelper.findMethod(WorldServer.class, "wakeAllPlayers", "func_73053_d");
		}

		try {
			wakeAllPlayers.invoke(world);
		} catch (Exception exception) {
			throw Throwables.propagate(exception);
		}
	}

	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if (e.phase == TickEvent.Phase.START) {
			if (e.world != null) {
				if (SAPIReferences.getSleepWakeManager().isEnabled()) {
					WorldServer world = (WorldServer) e.world;

					world.updateAllPlayersSleepingFlag();
					if (world.areAllPlayersAsleep())
						this.tryWakePlayers(world);
				}
			}
		}
	}

	private void tryWakePlayers(WorldServer world) {
		if (world.getGameRules().getBoolean("doDaylightCycle")) {
			WorldInfo info = world.getWorldInfo();
			info.setWorldTime(SAPIReferences.getSleepWakeManager().getWakeTime(world,
					(world.getWorldTime() / 24000L + 1L) * 24000L));
		}

		this.wakeAllPlayers(world);
	}

}
