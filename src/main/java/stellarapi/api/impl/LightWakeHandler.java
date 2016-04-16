package stellarapi.api.impl;

import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyProvider;
import stellarapi.api.mc.IWakeHandler;

public class LightWakeHandler implements IWakeHandler {

	private static final int DEFAULT_OFFSET = 1000;
	private double wakeAngle;
	
	public LightWakeHandler(double wakeAngle) {
		this.wakeAngle = wakeAngle;
	}
	
	@Override
	public long getWakeTime(World world, ISkyProvider skyProvider, long sleepTime) {
		double wakeDayOffset = skyProvider.dayOffsetUntilSunReach(this.wakeAngle);
		double currentDayOffset = skyProvider.getDaytimeOffset(sleepTime);
		double dayLength = skyProvider.getDayLength();

    	double modifiedWorldTime = sleepTime + (-wakeDayOffset - currentDayOffset) * dayLength;
    	while(modifiedWorldTime < sleepTime)
    		modifiedWorldTime += dayLength;
		return (long) modifiedWorldTime;
	}

	@Override
	public boolean canSleep(World world, ISkyProvider skyProvider, long sleepTime) {
		return !world.isDaytime();
	}

	@Override
	public long getWakeTime(World world, CelestialLightSources lightSource, ICelestialCoordinate coordinate,
			long sleepTime) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canSleep(World world, CelestialLightSources lightSource, ICelestialCoordinate coordinate,
			long sleepTime) {
		// TODO Auto-generated method stub
		return false;
	}

}
