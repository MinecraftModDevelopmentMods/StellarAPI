package stellarapi.api.mc;

import net.minecraft.world.World;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.config.IConfigHandler;

public interface IWakeHandler {
	
	/**
	 * Checks if this wake handler will accept certain case or not.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world
	 * @param coordinate the celestial coordinate for the world
	 * @return accept or not
	 * */
	public boolean accept(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate);
	
	/**
	 * Gets wake time for specific sleep time.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world
	 * @param coordinate the celestial coordinate for the world
	 * @param sleepTime specified sleep time in tick
	 * @return wake time in tick
	 * */
	public long getWakeTime(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime);
	
	/**
	 * Determine if it is able to sleep on specific time.
	 * @param world the world to control wake and sleep
	 * @param lightSource the light sources for the world
	 * @param coordinate the celestial coordinate for the world
	 * @param sleepTime specified sleep time in tick
	 * @return flag to determine possibility of sleep
	 * */
	public boolean canSleep(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime);
	
}
