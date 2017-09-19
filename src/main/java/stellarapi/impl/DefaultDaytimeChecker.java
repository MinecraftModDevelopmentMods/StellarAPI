package stellarapi.impl;

import net.minecraft.world.World;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.daywake.EnumDaytimeDescriptor;
import stellarapi.api.daywake.IDaytimeChecker;
import stellarapi.api.lib.math.Vector3;

/**
 * Default implementation for daytime checker when there is one main light
 * source, and celestial objects moves
 */
public class DefaultDaytimeChecker implements IDaytimeChecker {

	@Override
	public boolean accept(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor) {
		if (sources == null || coordinate == null)
			return false;

		Vector3 pos = sources.getPrimarySource().getCurrentAbsolutePos();

		switch (descriptor) {
		case MORNING:
		case AFTERNOON:
			return !Double
					.isNaN(coordinate.offsetTillObjectReach(pos, (Math.max(coordinate.getHighestHeightAngle(pos), 0.0)
							+ Math.max(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0));

		case EARLY_MORNING:
		case EVENING:
			return !Double
					.isNaN(coordinate.offsetTillObjectReach(pos, (Math.min(coordinate.getHighestHeightAngle(pos), 0.0)
							+ Math.min(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0));

		case DAWN:
		case DUSK:
			return !Double.isNaN(coordinate.offsetTillObjectReach(pos, 0.0));

		default:
			break;
		}

		return true;
	}

	@Override
	public boolean isDescriptorApply(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long time, int tolerance) {
		CelestialPeriod period = sources.getPrimarySource().getHorizontalPeriod();
		double currentOffset = period.getOffset(time, 0.0f);
		double toleranceOffset = tolerance / period.getPeriodLength();

		Vector3 pos = sources.getPrimarySource().getCurrentAbsolutePos();

		double riseOffset = coordinate.offsetTillObjectReach(pos, (Math.max(coordinate.getHighestHeightAngle(pos), 0.0)
				+ Math.max(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0);

		double fallOffset = coordinate.offsetTillObjectReach(pos, (Math.min(coordinate.getHighestHeightAngle(pos), 0.0)
				+ Math.min(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0);

		double horizonOffset = coordinate.offsetTillObjectReach(pos, 0.0);

		switch (descriptor) {
		case MIDNIGHT:
			return currentOffset < toleranceOffset || currentOffset > 1.0 - toleranceOffset;
		case MIDDAY:
			return Math.abs(0.5 - currentOffset) < toleranceOffset;

		case EARLY_MORNING:
			return Math.abs(currentOffset - fallOffset) < toleranceOffset;
		case MORNING:
			return Math.abs(currentOffset - riseOffset) < toleranceOffset;
		case AFTERNOON:
			return Math.abs(currentOffset - 1.0 + riseOffset) < toleranceOffset;
		case EVENING:
			return Math.abs(currentOffset - 1.0 + fallOffset) < toleranceOffset;

		case DAWN:
			return Math.abs(currentOffset - horizonOffset) < toleranceOffset;
		case DUSK:
			return Math.abs(currentOffset - 1.0 + horizonOffset) < toleranceOffset;
		}

		return false;
	}

	@Override
	public long timeForCertainDescriptor(World world, CelestialEffectors sources, ICelestialCoordinate coordinate,
			EnumDaytimeDescriptor descriptor, long currentTime) {
		CelestialPeriod period = sources.getPrimarySource().getHorizontalPeriod();

		Vector3 pos = sources.getPrimarySource().getCurrentAbsolutePos();

		double riseOffset = coordinate.offsetTillObjectReach(pos, (Math.max(coordinate.getHighestHeightAngle(pos), 0.0)
				+ Math.max(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0);

		double fallOffset = coordinate.offsetTillObjectReach(pos, (Math.min(coordinate.getHighestHeightAngle(pos), 0.0)
				+ Math.min(coordinate.getLowestHeightAngle(pos), 0.0)) / 2.0);

		double horizonOffset = coordinate.offsetTillObjectReach(pos, 0.0);

		switch (descriptor) {
		case MIDNIGHT:
			return period.getTimeForOffset(currentTime, 0.0);
		case MIDDAY:
			return period.getTimeForOffset(currentTime, 0.5);

		case EARLY_MORNING:
			if (!Double.isNaN(fallOffset))
				return period.getTimeForOffset(currentTime, fallOffset);
			break;
		case MORNING:
			if (!Double.isNaN(riseOffset))
				return period.getTimeForOffset(currentTime, riseOffset);
			break;
		case AFTERNOON:
			if (!Double.isNaN(riseOffset))
				return period.getTimeForOffset(currentTime, 1.0 - riseOffset);
		case EVENING:
			if (!Double.isNaN(fallOffset))
				return period.getTimeForOffset(currentTime, 1.0 - fallOffset);

		case DAWN:
			if (!Double.isNaN(horizonOffset))
				return period.getTimeForOffset(currentTime, horizonOffset);
			break;
		case DUSK:
			if (!Double.isNaN(horizonOffset))
				return period.getTimeForOffset(currentTime, 1.0 - horizonOffset);
			break;
		}

		return currentTime;
	}

}
