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
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.helper.LivingItemAccessHelper;
import stellarapi.api.interact.IOpticalProperties;

public class StellarAPITickHandler {

	private Method wakeAllPlayers = null;
	
	private void wakeAllPlayers(WorldServer world) {
		if(this.wakeAllPlayers == null) {
			this.wakeAllPlayers = ReflectionHelper.findMethod(WorldServer.class,
					world, ObfuscationReflectionHelper.remapFieldNames(WorldServer.class.getName(), "wakeAllPlayers", "func_73053_d"));
		}

		try {
			wakeAllPlayers.invoke(world);
		} catch (Exception exception) {
			throw Throwables.propagate(exception);
		}
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void livingUpdate(LivingUpdateEvent event) {
		if(!StellarAPIReference.isOpticalEntity(event.getEntityLiving()))
			return;

		EnumHand hand = event.getEntityLiving().getActiveHand();
		ItemStack itemstack = hand != null ? event.getEntityLiving().getHeldItem(hand) : ItemStack.EMPTY;
		ItemStack itemInUse = event.getEntityLiving().getActiveItemStack();

		if (!ItemStack.areItemStacksEqual(itemstack, itemInUse)) {
			boolean updateScope = false;
			boolean updateFilter = false;

			if (itemstack.hasCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemstack.getCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if (itemInUse.hasCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP)) {
				IOpticalProperties property = itemInUse.getCapability(StellarAPICapabilities.OPTICAL_PROPERTY, EnumFacing.UP);
				updateScope = updateScope || property.isScope();
				updateFilter = updateFilter || property.isFilter();
			}

			if(updateScope) {
				LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), itemInUse == ItemStack.EMPTY? ItemStack.EMPTY : itemstack);
				StellarAPIReference.updateScope(event.getEntityLiving());
			}

			if(updateFilter) {
				LivingItemAccessHelper.setUsingItem(event.getEntityLiving(), itemInUse == ItemStack.EMPTY? ItemStack.EMPTY :  itemstack);
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
				}
			}
		}
	}

	private void tryWakePlayers(WorldServer world) {
		if (world.getGameRules().getBoolean("doDaylightCycle")) {
			WorldInfo info = world.getWorldInfo();
			info.setWorldTime(StellarAPIReference.getSleepWakeManager().getWakeTime(world,
					(world.getWorldTime() / 24000L + 1L) * 24000L));
		}

		this.wakeAllPlayers(world);
	}

}
