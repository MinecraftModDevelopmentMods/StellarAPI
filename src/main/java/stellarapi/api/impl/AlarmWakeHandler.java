package stellarapi.api.impl;

import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.CelestialLightSources;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyProvider;
import stellarapi.api.mc.IWakeHandler;

public class AlarmWakeHandler implements IWakeHandler {

	//Wake time from midnight
	private int wakeTime;

	public AlarmWakeHandler(int wakeTime) {
		this.wakeTime = wakeTime;
	}

	@Override
	public long getWakeTime(World world, ISkyProvider skyProvider, long sleepTime) {
		double currentOffset = skyProvider.getDaytimeOffset(sleepTime);
		double dayLength = skyProvider.getDayLength();
		double modifiedWorldTime = this.wakeTime - (currentOffset - 0.25) * dayLength;
    	while(modifiedWorldTime < sleepTime)
    		modifiedWorldTime += dayLength;
		return (long) modifiedWorldTime;
	}

	@Override
	public boolean canSleep(World world, ISkyProvider skyProvider, long sleepTime) {    	
    	return !world.isDaytime() && skyProvider.getDaytimeOffset(sleepTime) > 0.5;
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
