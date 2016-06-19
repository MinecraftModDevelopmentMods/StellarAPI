package stellarapi;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
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
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.interact.ApplyOpticalItemEvent;
import stellarapi.api.event.interact.CheckSameOpticalItemEvent;
import stellarapi.api.helper.LivingItemAccessHelper;
import stellarapi.api.optics.IOpticalViewer;
import stellarapi.reference.OpticalViewerEventCallback;

public class StellarAPITickHandler {

	private Field sleep;

	public StellarAPITickHandler() {
		sleep = getField(WorldServer.class, "allPlayersSleeping", "field_73068_P");
	}

	public static Field getField(Class<?> clazz, String... fieldNames) {
		return ReflectionHelper.findField(clazz,
				ObfuscationReflectionHelper.remapFieldNames(clazz.getName(), fieldNames));
	}

	@SubscribeEvent
	public void livingUpdate(LivingUpdateEvent e) {
		EnumHand hand = e.getEntityLiving().getActiveHand();
		ItemStack itemstack = hand != null ? e.getEntityLiving().getHeldItem(e.getEntityLiving().getActiveHand())
				: null;
		ItemStack itemInUse = LivingItemAccessHelper.getUsingItem(e.getEntityLiving());

		if (itemInUse != null) {
			CheckSameOpticalItemEvent checkEvent = new CheckSameOpticalItemEvent(e.getEntityLiving(), itemstack,
					itemInUse);
			boolean flag = StellarAPIReference.getEventBus().post(checkEvent);

			if (flag || !checkEvent.isSame()) {
				ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(e.getEntityLiving(), itemInUse);
				StellarAPIReference.getEventBus().post(applyEvent);

				if (applyEvent.isViewScope() || applyEvent.isOpticalFilter())
					e.getEntityLiving().resetActiveHand();
				if (applyEvent.isViewScope())
					StellarAPIReference.updateScope(e.getEntityLiving());
				if (applyEvent.isOpticalFilter())
					StellarAPIReference.updateFilter(e.getEntityLiving());
			} else if (itemstack != itemInUse)
				LivingItemAccessHelper.setUsingItem(e.getEntityLiving(), itemstack);
		}

		IOpticalViewer viewer = e.getEntityLiving().getCapability(StellarAPICapabilities.VIEWER_CAPABILITY,
				EnumFacing.DOWN);
		if (viewer instanceof OpticalViewerEventCallback)
			((OpticalViewerEventCallback) viewer).update();
	}

	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if (e.phase == TickEvent.Phase.START) {
			if (e.world != null) {
				if (StellarAPIReference.getSleepWakeManager().isEnabled()) {
					WorldServer world = (WorldServer) e.world;

					world.updateAllPlayersSleepingFlag();
					if (world.areAllPlayersAsleep())
						this.tryWakePlayers(world);

					try {
						sleep.setBoolean(world, false);
					} catch (IllegalArgumentException ex) {
						ex.printStackTrace();
					} catch (IllegalAccessException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private void tryWakePlayers(WorldServer world) {
		if (world.getGameRules().getBoolean("doDaylightCycle")) {
			WorldInfo info = world.getWorldInfo();
			long worldTime = info.getWorldTime();
			info.setWorldTime(StellarAPIReference.getSleepWakeManager().getWakeTime(world,
					(world.getWorldTime() / 24000L + 1L) * 24000L));
		}

		Iterator iterator = world.playerEntities.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();

			if (entityplayer.isPlayerSleeping()) {
				entityplayer.wakeUpPlayer(false, false, true);
			}
		}

		world.provider.resetRainAndThunder();
	}

}
