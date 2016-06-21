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
import stellarapi.api.helper.LivingItemAccessHelper;
import stellarapi.api.interact.IOpticalProperties;

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
	public void livingUpdate(LivingUpdateEvent event) {
		EnumHand hand = event.getEntityLiving().getActiveHand();
		ItemStack itemstack = hand != null ? event.getEntityLiving().getHeldItem(hand) : null;
		ItemStack itemInUse = event.getEntityLiving().getActiveItemStack();

		if (!ItemStack.areItemStacksEqual(itemstack, itemInUse)) {
			boolean updateScope = false;
			boolean updateFilter = false;

			if (itemstack != null && itemstack.hasCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemstack.getCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if (itemInUse != null && itemInUse.hasCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemInUse.getCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if(updateScope) {
				LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), itemInUse == null? null : itemstack);
				StellarAPIReference.updateScope(event.getEntityLiving());
			}

			if(updateFilter) {
				LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), itemInUse == null? null :  itemstack);
				StellarAPIReference.updateFilter(event.getEntityLiving());
			}
		} else if(itemstack != itemInUse)
			LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), itemstack);
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
