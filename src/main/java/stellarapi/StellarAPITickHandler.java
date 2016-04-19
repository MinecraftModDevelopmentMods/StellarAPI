package stellarapi;

import java.lang.reflect.Field;
import java.util.Iterator;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.helper.PlayerItemAccessHelper;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;

public class StellarAPITickHandler {
	
	private Field sleep;
			
	public StellarAPITickHandler() {
		sleep = getField(WorldServer.class, "allPlayersSleeping", "field_73068_P");
	}
	
	public static Field getField(Class<?> clazz, String... fieldNames) {
		return ReflectionHelper.findField(clazz, ObfuscationReflectionHelper.remapFieldNames(clazz.getName(), fieldNames));
	}
	
	@SubscribeEvent
	public void tickStart(TickEvent.PlayerTickEvent e) {
		if(e.phase == Phase.START) {
			ItemStack itemstack = e.player.getCurrentEquippedItem();
			ItemStack itemInUse = PlayerItemAccessHelper.getUsingItem(e.player);
						
            if (itemstack != itemInUse) {
    			e.player.clearItemInUse();

            	if(itemInUse != null && itemInUse.getItem() instanceof IViewScope)
        			StellarAPIReference.updateScope(e.player);
            	if(itemInUse != null && itemInUse.getItem() instanceof IOpticalFilter)
        			StellarAPIReference.updateFilter(e.player);
            }
		}
	}
		
	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if(e.phase == Phase.START){
			if(e.world != null) {
				if(StellarAPIReference.getSleepWakeManager().isEnabled()) {
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
        if (world.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
        	WorldInfo info = world.getWorldInfo();
        	long worldTime = info.getWorldTime();
            info.setWorldTime(StellarAPIReference.getSleepWakeManager().getWakeTime(
            		world, (world.getWorldTime() / 24000L + 1L) * 24000L));
        }

        Iterator iterator = world.playerEntities.iterator();

        while (iterator.hasNext())
        {
            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (entityplayer.isPlayerSleeping())
            {
                entityplayer.wakeUpPlayer(false, false, true);
            }
        }

        world.provider.resetRainAndThunder();
	}

}
