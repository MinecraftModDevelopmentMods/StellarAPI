package stellarapi.api.daywake;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.World;
import stellarapi.api.SAPIReferences;

public class DaytimeChecker {

	private List<IDaytimeChecker> daytimeCheckers = Lists.newArrayList();

	/**
	 * Registers daytime checker handler.
	 * 
	 * @param checker
	 *            the daytime checker to register
	 */
	public void registerDaytimeChecker(IDaytimeChecker checker) {
		daytimeCheckers.add(0, checker);
	}

	/**
	 * Checks if certain descriptor applies in certain tolerance.
	 * 
	 * @param world
	 *            the world
	 * @param sources
	 *            the celestial light sources
	 * @param descriptor
	 *            the daytime descriptor
	 * @param time
	 *            the time, should be in the same day or the next/previous day
	 *            from now
	 * @param tolerance
	 *            the time tolerance in tick
	 * @param defaultApply
	 *            the value to return when no daytime checkers is detected or
	 *            stellar settings is invalid to check daytime settings
	 */
	public boolean isDescriptorApply(World world, EnumDaytimeDescriptor descriptor, long time, int tolerance,
			boolean defaultApply) {
		ICelestialCoordinate coordinate = SAPIReferences.getCoordinate(world);
		CelestialEffectors lightSources = SAPIReferences.getEffectors(world, IEffectorType.Light);

		for (IDaytimeChecker checker : this.daytimeCheckers) {
			if (checker.accept(world, lightSources, coordinate, descriptor))
				return checker.isDescriptorApply(world, lightSources, coordinate, descriptor, time, tolerance);
		}

		return defaultApply;
	}

	/**
	 * Calculates time for certain descriptor, starting from now.
	 * 
	 * @param world
	 *            the world
	 * @param descriptor
	 *            the daytime descriptor
	 * @param defaultValue
	 *            the value to return when no daytime checkers is detected or
	 *            stellar settings is invalid to check daytime settings
	 */
	public long timeForCertainDescriptor(World world, EnumDaytimeDescriptor descriptor, long defaultValue) {
		ICelestialCoordinate coordinate = SAPIReferences.getCoordinate(world);
		CelestialEffectors lightSources = SAPIReferences.getEffectors(world, IEffectorType.Light);

		for (IDaytimeChecker checker : this.daytimeCheckers) {
			if (checker.accept(world, lightSources, coordinate, descriptor))
				return checker.timeForCertainDescriptor(world, lightSources, coordinate, descriptor,
						world.getWorldTime());
		}

		return defaultValue;
	}

}
