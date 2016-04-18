package stellarapi.api.daywake;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.celestials.CelestialLightSources;
import stellarapi.api.lib.config.IConfigHandler;

/**
 * Determine player's sleep availability and wake time.<p>
 * Note that wake handler which is registered later will have higher priority.<p>
 * Also, each wake handler will be registered to wake configuration,
 * so it would be configurable using Stellar API config.
 * */
public interface IWakeHandler extends IConfigHandler {
	
	/**
	 * Checks if this wake handler will accept certain case or not.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world (only dependent to the world)
	 * @param coordinate the celestial coordinate for the world (only dependent to the world)
	 * @return accept or not
	 * */
	public boolean accept(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate);
	
	/**
	 * Gets wake time for specific sleep time.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world (only dependent to the world)
	 * @param coordinate the celestial coordinate for the world (only dependent to the world)
	 * @param sleepTime specified sleep time in tick (only dependent to the world)
	 * @return wake time in tick
	 * */
	public long getWakeTime(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime);
	
	/**
	 * Determine if it is able to sleep on specific time. <p>
	 * Note that this is checked on all wake handlers,
	 * and stops on handler with maximum priority which does not give OK as result.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world
	 * @param coordinate the celestial coordinate for the world
	 * @param sleepTime specified sleep time in tick
	 * @return flag to determine possibility of sleep
	 * */
	public EntityPlayer.EnumStatus getSleepPossibility(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime);
	
}
