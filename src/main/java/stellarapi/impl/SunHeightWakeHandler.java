package stellarapi.impl;

import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.celestials.CelestialLightSources;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.mc.IWakeHandler;

/**
 * Example of wake handler,
 * as player gets up when the primary light source reaches certain angle.
 * */
public class SunHeightWakeHandler implements IWakeHandler {

	private double wakeAngle;
	
	@Override
	public boolean accept(World world, CelestialLightSources lightSource, ICelestialCoordinate coordinate) {
		return true;
	}
	
	
	@Override
	public long getWakeTime(World world, CelestialLightSources lightSource, ICelestialCoordinate coordinate,
			long sleepTime) {
		ICelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentAbsolutePos(), this.wakeAngle);
		return sun.getHorizontalPeriod().getTimeForOffset(sleepTime, offset);
	}

	@Override
	public EnumStatus getSleepPossibility(World world, CelestialLightSources lightSource,
			ICelestialCoordinate coordinate, long sleepTime) {
		ICelestialObject sun = lightSource.getPrimarySource();
		double offset = coordinate.offsetTillObjectReach(sun.getCurrentAbsolutePos(), this.wakeAngle);
		if(Double.isNaN(offset))
			return EnumStatus.NOT_POSSIBLE_HERE;
		return world.provider.isDaytime()? EnumStatus.OK : EnumStatus.NOT_POSSIBLE_NOW;
	}

	
	@Override
	public void setupConfig(Configuration config, String category) {
		Property wakeAngle = config.get(category, "Sun_Height_for_Wake", 10.0);
		wakeAngle.comment = "Solar azimuth(height) angle to wake up. (in degrees)";
		wakeAngle.setRequiresWorldRestart(true);
		wakeAngle.setLanguageKey("config.property.wakeangle");
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		ConfigCategory cfgCategory = config.getCategory(category);
		this.wakeAngle = cfgCategory.get("Sun_Height_for_Wake").getDouble();
	}

	@Override
	public void saveToConfig(Configuration config, String category) { }

}
