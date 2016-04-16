package stellarapi;

import java.lang.reflect.Field;
import java.util.Iterator;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import stellarapi.api.ISkyProvider;
import stellarapi.api.StellarAPIReference;
import stellarapi.stellars.StellarManager;
import stellarapi.stellars.view.StellarDimensionManager;

public class StellarTickHandler {
	
	private Field sleep;
			
	public StellarTickHandler() {
		sleep = getField(WorldServer.class, "allPlayersSleeping", "field_73068_P");
	}
	
	public static Field getField(Class<?> clazz, String... fieldNames) {
		return ReflectionHelper.findField(clazz, ObfuscationReflectionHelper.remapFieldNames(clazz.getName(), fieldNames));
	}
		
	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if(e.phase == Phase.START){
			if(e.world != null) {
				StellarDimensionManager dimManager = StellarDimensionManager.get(e.world);
				if(dimManager != null)
					dimManager.update(e.world, e.world.getWorldTime());
				
				StellarManager manager = StellarManager.getManager(false);
				if(!StellarAPIReference.hasSkyProvider(e.world))
					return;
				
				if(StellarAPI.proxy.wakeManager.isEnabled()) {
					WorldServer world = (WorldServer) e.world;

					world.updateAllPlayersSleepingFlag();
					if (world.areAllPlayersAsleep())
						this.tryWakePlayers(world, StellarAPIReference.getSkyProvider(e.world));

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

	private void tryWakePlayers(WorldServer world, ISkyProvider skyProvider) {		
        if (world.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
        	WorldInfo info = world.getWorldInfo();
        	long worldTime = info.getWorldTime();
            info.setWorldTime(StellarAPI.proxy.wakeManager.getWakeTime(world, skyProvider, worldTime));
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
