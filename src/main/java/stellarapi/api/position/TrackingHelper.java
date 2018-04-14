package stellarapi.api.position;

import java.sql.Time;
import java.util.List;

public class TrackingHelper {
	// TODO Move to the implementation section after celestial system overhaul
	// FIXME Work on this + Find a way to find first root of transformations. Allow numerical differentiation?

	// Is this small enough?
	public static final double EPSILON = 1.0e-5;

	/**
	 * Calculates the first time when the object reaches given height after given time.
	 * @param trajectory the trajectory
	 * @param transforms the transforms applied
	 * @param height the height
	 * @param startTime the starting time
	 * @param minTime minimal time after starting time to search, or search accuracy
	 * @param maxtime maximal time after starting time to search, or search duration
	 * */
	public static double calculateTime(ICTrajectory trajectory, List<ICTransform> transforms,
			double height, double startTime, double minTime, double maxTime) {
		return 0.0;
	}

	/**
	 * Calculates the first time when the object reaches given local extremum after given time.
	 * @param trajectory the trajectory
	 * @param transforms the transforms applied
	 * @param forMax whether this is for maximum or minimum
	 * @param startTime the starting time
	 * @param minTime minimal time after starting time to search, or search accuracy
	 * @param maxtime maximal time after starting time to search, or search duration
	 * */
	public static double calculateLocalExtTime(ICTrajectory trajectory, List<ICTransform> transforms,
			boolean forMax, double startTime, double minTime, double maxTime) {
		return 0.0;
	}
}
