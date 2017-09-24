package stellarapi.api.instruments;

import stellarapi.api.coordinates.CCoordinates;
import stellarapi.api.lib.math.SpCoord;

public interface IMount {
	/**
	 * Gets maximal move speed in rad/s.
	 * */
	public double getMoveSpeed();

	/**
	 * Gets coordinates this mount is based on.
	 * */
	public CCoordinates getBaseCoordinates();

	/**
	 * Calculates the view direction on the mount coordinates.
	 * */
	public SpCoord getViewDirection();
}