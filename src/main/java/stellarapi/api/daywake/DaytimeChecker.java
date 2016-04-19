package stellarapi.api.daywake;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.celestials.CelestialEffectors;

public class DaytimeChecker {
	
	private List<IDaytimeChecker> daytimeCheckers = Lists.newArrayList();
	
	/**
	 * Registers daytime checker handler.
	 * @param checker the daytime checker to register
	 * */
	public void registerDaytimeChecker(IDaytimeChecker checker) {
		daytimeCheckers.add(0, checker);
	}
	
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
			EnumDaytimeDescriptor descriptor, long time, int tolerance) {
		ICelestialCoordinate coordinate = StellarAPIReference.getCoordinate(world);
		CelestialEffectors lightSources = StellarAPIReference.getLightSources(world);
		
		if(coordinate != null && lightSources != null)
			for(IDaytimeChecker checker : this.daytimeCheckers) {
				if(checker.accept(world, lightSources, coordinate, descriptor))
					return checker.isDescriptorApply(world, lightSources, coordinate, descriptor, time, tolerance);
			}
		
		return false;
	}
	
	/**
	 * Calculates time for certain descriptor, starting from now.
	 * @param world the world
	 * @param descriptor the daytime descriptor
	 * @param defaultValue the value to return when no daytime checkers is detected
	 * */
	public long timeForCertainDescriptor(World world,
			EnumDaytimeDescriptor descriptor, long defaultValue) {
		ICelestialCoordinate coordinate = StellarAPIReference.getCoordinate(world);
		CelestialEffectors lightSources = StellarAPIReference.getLightSources(world);
		
		if(coordinate != null && lightSources != null)		
			for(IDaytimeChecker checker : this.daytimeCheckers) {
				if(checker.accept(world, lightSources, coordinate, descriptor))
					return checker.timeForCertainDescriptor(world, lightSources, coordinate, descriptor, world.getWorldTime());
		}
		
		return defaultValue;
	}

}
