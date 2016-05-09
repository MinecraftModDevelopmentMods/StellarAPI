package stellarapi;

import java.lang.reflect.Field;
import java.util.Iterator;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.interact.ApplyOpticalItemEvent;
import stellarapi.api.event.interact.CheckSameOpticalItemEvent;
import stellarapi.api.helper.PlayerItemAccessHelper;
import stellarapi.reference.PerEntityManager;

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
			
            if (itemInUse != null) {
            	CheckSameOpticalItemEvent checkEvent = new CheckSameOpticalItemEvent(e.player, itemstack, itemInUse);
            	boolean flag = StellarAPIReference.getEventBus().post(checkEvent);
            	
            	if(flag || !checkEvent.isSame()) {
            		ApplyOpticalItemEvent applyEvent = new ApplyOpticalItemEvent(e.player, itemInUse);
            		StellarAPIReference.getEventBus().post(applyEvent);
            		
            		if(applyEvent.isViewScope() || applyEvent.isOpticalFilter())
            			e.player.clearItemInUse();
            		if(applyEvent.isViewScope())
            			StellarAPIReference.updateScope(e.player);
            		if(applyEvent.isOpticalFilter())
            			StellarAPIReference.updateFilter(e.player);
            	} else if(itemstack != itemInUse)
            		PlayerItemAccessHelper.setUsingItem(e.player, itemstack);
            }
            
    		if(!PerEntityManager.hasEntityManager(e.player))
    			PerEntityManager.registerEntityManager(e.player);
            PerEntityManager.getEntityManager(e.player).updateRiding();
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
