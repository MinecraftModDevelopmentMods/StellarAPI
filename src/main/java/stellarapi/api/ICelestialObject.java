package stellarapi.api;

import javax.vecmath.Vector3d;

import stellarapi.util.math.SpCoord;

/**
 * Celestial Object interface. <p> 
 * Note that this object is not the static celestial object itself,
 *  but the image of the object which can be seen from specific world. <p>
 * So this object is, indeed, per-dimensional.
 * */
public interface ICelestialObject {
	
	/**
	 * Horizontal period for the effector. <p>
	 * This is the period that the horizontal position of this effector returns to original position.
	 * @return
	 * <ul>
	 * <li>horizontal period object for this effector if it exists
	 * <li><b><code>null</code></b> if this effector is stopped or have random movements.
	 * </ul>
	 * */
	public ICelestialPeriod getHorizontalPeriod();
	
	/**
	 * Phase period for this effector.
	 * @return
	 * <ul>
	 * <li>phase period object for this effector,
	 * <li><b><code>null</code></b> if this effector doesn't have phase, e.g. sun.
	 * </ul>
	 * */
	public ICelestialPeriod getPhasePeriod();
	
	/**
	 * Gets current phase of this effector.
	 * */
	public double getCurrentPhase();
	
	/**
	 * Gets current relative brightness to the maximum brightness.
	 * */
	public double getCurrentBrightness();
	
	/** 
	 * Gets current absolute position. <p>
	 * Note that the center is still on the ground, <p>
	 * and this position is per dimension.
	 * */
	public Vector3d getCurrentAbsolutePos();
	
	/** Gets current position for horizontal coordinate. */
	public SpCoord getCurrentHorizontalPos();

}
