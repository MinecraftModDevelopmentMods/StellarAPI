package stellarapi.api.world;

import stellarapi.api.coordinates.CoordContext;

public interface IWorldEffectHandler {
	// TODO fill in this world effect handler

	/** Evaluates celestial angle */
	public double calculateCelestialAngle(CoordContext context, float partialTicks);

	/** Gets lunar phase. Usually, second brightest object is served as a moon. */
	public double getMoonPhase(CoordContext context);

}