package stellarapi.api.celestials;

public enum EnumCelestialObjectType {
	/**
	 * Celestial light source which can emit light.
	 */
	Star,

	/**
	 * Celestial object which cannot emit light and revolves around a celestial
	 * body, but has enough mass to have local gravitation influence zone and
	 * form spherical shape.
	 */
	Planet,

	/**
	 * Celestial object which cannot emit light and revolves around a planet,
	 * but form spherical shape.
	 */
	Satellite,

	/**
	 * Small celestial object which is neither {@linkplain #Star star} nor
	 * {@linkplain #Planet planet}.
	 */
	Asteroid,

	/**
	 * Group of small celestial objects which are near to each other
	 */
	AsteroidGroup,

	/**
	 * Special type of small celestial object which has highly eccentric orbit,
	 * and emits lots of its own mass to space. This emission is mainly caused
	 * by solar(or stellar) wind.
	 */
	Comet,

	/**
	 * Deep sky object, such as nebula and clusters.
	 */
	DeepSkyObject;
}