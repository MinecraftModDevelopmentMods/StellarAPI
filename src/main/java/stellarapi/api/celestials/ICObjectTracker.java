package stellarapi.api.celestials;

import stellarapi.api.position.ICTrajectory;
import stellarapi.api.position.ICTransform;

/**
 * <p>Tracker for certain celestial object.</p>
 * <p>It will receive update when current tick is out of range of [last tick updated, tick to update).</p>
 * */
public interface ICObjectTracker {
	/**
	 * Update method.
	 * @param currentTick current tick
	 * @param trajectory the trajectory of the tracking object
	 * @param transform the relevant transformations based on horizontal coordinates
	 * @return the next tick to update
	 * */
	public long onUpdate(long currentTick, ICTrajectory trajectory, ICTransform[] transforms);
}
