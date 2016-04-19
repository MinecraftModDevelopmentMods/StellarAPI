package stellarapi.api;

import net.minecraft.world.World;

public class PeriodHelper {
	
	/**
	 * Gets common day period for primary light source.
	 * @param world the world
	 * */
	public static CelestialPeriod getDayPeriod(World world) {
		return StellarAPIReference.getLightSources(world).getPrimarySource()
				.getHorizontalPeriod();
	}
	
	/**
	 * Gets common year period for primary light source.
	 * @param world the world
	 * */
	public static CelestialPeriod getYearPeriod(World world) {
		return StellarAPIReference.getLightSources(world).getPrimarySource()
				.getAbsolutePeriod();
	}

}
