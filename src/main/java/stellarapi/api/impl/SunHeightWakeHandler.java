package stellarapi.api.impl;

import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ICelestialObject;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveToConfig(Configuration config, String category) { }

}
