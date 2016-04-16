package stellarapi.api.mc;

import net.minecraft.world.World;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;

/**
 * Daytime checker which checks relation with world time in tick and daytime descriptor. <p>
 * Note that daytime checker which is registered later will have higher priority.
 * */
public interface IDaytimeChecker {
	
	/**
	 * Checks if this daytime checker will work on certain case or not.
	 * @param world the world
	 * @param sources the celestial light sources
	 * @param coordinate the coordinate
	 * @param descriptor the daytime descriptor
	 * */
	public boolean accept(World world,
			CelestialLightSources sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor);
	
	/**
	 * Checks if certain descriptor applies in certain tolerance.
	 * @param world the world
	 * @param sources the celestial light sources
	 * @param coordinate the coordinate
	 * @param descriptor the daytime descriptor
	 * @param time the time, should be in the same day or the next/previous day from now
	 * @param tolerance the time tolerance in tick
	 * */
	public boolean isDescriptorApply(World world,
			CelestialLightSources sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long time, int tolerance);
	
	/**
	 * Calculates time for certain descriptor, starting from now.
	 * @param world the world
	 * @param sources the celestial light sources
	 * @param coordinate the coordinate
	 * @param descriptor the daytime descriptor
	 * @param currentTime the current time
	 * */
	public long timeForCertainDescriptor(World world,
			CelestialLightSources sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long currentTime);

}