package stellarapi.impl;

import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.daywake.IWakeHandler;

/**
 * Example of wake handler, as player gets up when the primary light source
 * reaches certain angle.
 */
public class SunHeightWakeHandler implements IWakeHandler {

	@Config.Name("Sun_Height_for_Wake")
	@Config.RangeDouble(min = -90.0, max = 90.0)
	@Config.LangKey("config.property.wakeangle")
	@Config.Comment("Solar azimuth(height) angle to wake up. (in degrees)")
	@Config.RequiresWorldRestart
	private double wakeAngle = 10.0;

	@Override
	public boolean accept(World world, CelestialEffectors lightSource, ICelestialCoordinate coordinate) {
		return lightSource != null && coordinate != null;
	}

	@Override
	public long getWakeTime(World world, CelestialEffectors lightSource, ICelestialCoordinate coordinate,
			long sleepTime) {
		ICelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentAbsolutePos(), this.wakeAngle);
		return sun.getHorizontalPeriod().getTimeForOffset(sleepTime, offset);
	}

	@Override
	public SleepResult getSleepPossibility(World world, CelestialEffectors lightSource, ICelestialCoordinate coordinate,
			long sleepTime) {
		ICelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentAbsolutePos(), this.wakeAngle);
		if (Double.isNaN(offset))
			return SleepResult.NOT_POSSIBLE_HERE;
		return world.provider.isDaytime() ? SleepResult.NOT_POSSIBLE_NOW : SleepResult.OK;
	}

	public void setupConfig(Configuration config, String category) {
		config.setCategoryLanguageKey(category, "config.category.sunheight");
		config.setCategoryComment(category, "Sun height type wake settings");
	}

}
