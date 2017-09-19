package stellarapi.api.daywake;

import net.minecraft.world.World;

/**
 * Daytime checker which checks relation with world time in tick and daytime
 * descriptor.
 * <p>
 * Mostly co-implemented with the coordinate provider.
 * <p>
 * Note that daytime checker which is registered later will have higher
 * priority.
 */
public interface IDaytimeChecker {

	/**
	 * Checks if this daytime checker will work on certain case or not.
	 * <p>
	 * Should return false when
	 * {@link #timeForCertainDescriptor(World, CelestialEffectors, ICelestialCoordinate, EnumDaytimeDescriptor, long)}
	 * } can't give right value.
	 * 
	 * @param world
	 *            the world
	 * @param sources
	 *            the celestial light sources, can be null (only dependent to
	 *            the world)
	 * @param coordinate
	 *            the coordinate, can be null (only dependent to the world)
	 * @param descriptor
	 *            the daytime descriptor
	 */
	public boolean accept(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor);

	/**
	 * Checks if certain descriptor applies in certain tolerance.
	 * 
	 * @param world
	 *            the world
	 * @param sources
	 *            the celestial light sources
	 * @param coordinate
	 *            the coordinate (only dependent to the world)
	 * @param descriptor
	 *            the daytime descriptor (only dependent to the world)
	 * @param time
	 *            the time, should be in the same day or the next/previous day
	 *            from now
	 * @param tolerance
	 *            the time tolerance in tick
	 * @return if the time is appropriate for the descriptor or not
	 */
	public boolean isDescriptorApply(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long time, int tolerance);

	/**
	 * Calculates time for certain descriptor, starting from now.
	 * 
	 * @param world
	 *            the world
	 * @param sources
	 *            the celestial light sources
	 * @param coordinate
	 *            the coordinate
	 * @param descriptor
	 *            the daytime descriptor
	 * @param currentTime
	 *            the current time
	 * @return time for the descriptor after current time
	 * @throws ImpossibleDescriptorException
	 *             if there is no time for the certain descriptor
	 */
	public long timeForCertainDescriptor(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long currentTime);

}