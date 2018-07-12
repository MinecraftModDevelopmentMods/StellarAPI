package stellarapi.impl.wake;

import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.daywake.IWakeHandler;
import stellarapi.api.view.ICCoordinates;

/**
 * Example of wake handler, as player gets up when the primary light source
 * reaches certain angle.
 */
public class SunHeightWakeHandler implements IWakeHandler {

	private double wakeAngle;

	@Override
	public boolean accept(World world, CelestialEffectors lightSource, ICCoordinates coordinate) {
		return lightSource != null && coordinate != null;
	}

	@Override
	public long getWakeTime(World world, CelestialEffectors lightSource, ICCoordinates coordinate,
			long sleepTime) {
		CelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentPos(), this.wakeAngle);
		return sun.getHorizontalPeriod(coordinate).getTimeForOffset(sleepTime, offset);
	}

	@Override
	public SleepResult getSleepPossibility(World world, CelestialEffectors lightSource, ICCoordinates coordinate,
			long sleepTime) {
		CelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentPos(), this.wakeAngle);
		if (Double.isNaN(offset))
			return SleepResult.NOT_POSSIBLE_HERE;
		return world.provider.isDaytime() ? SleepResult.NOT_POSSIBLE_NOW : SleepResult.OK;
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryLanguageKey(category, "config.category.sunheight");
		config.setCategoryComment(category, "Sun height type wake settings");

		Property wakeAngle = config.get(category, "Sun_Height_for_Wake", 10.0);
		wakeAngle.setComment("Solar azimuth(height) angle to wake up. (in degrees)");
		wakeAngle.setRequiresWorldRestart(true);
		wakeAngle.setLanguageKey("config.property.wakeangle");
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		ConfigCategory cfgCategory = config.getCategory(category);
		this.wakeAngle = cfgCategory.get("Sun_Height_for_Wake").getDouble();
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
	}

}
