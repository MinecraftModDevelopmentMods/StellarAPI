package stellarapi.api.celestials;

public enum EnumCelestialCategory {
	/**
	 * Unknown, defaults to 0
	 * */
	UNKNOWN,

	/**
	 * Celestial light source which can emit light.
	 */
	STAR,

	/**
	 * Celestial object which cannot emit light and revolves around a celestial
	 * body, but has enough mass to have local gravitation influence zone and
	 * form spherical shape.
	 */
	PLANET,

	/**
	 * Celestial object which cannot emit light and revolves around a planet,
	 * but form spherical shape.
	 */
	MOON,

	/**
	 * Small celestial object which is neither {@linkplain #Star star} nor
	 * {@linkplain #Planet planet}.
	 */
	ASTEROID,

	/**
	 * Group of small celestial objects which are near to each other
	 */
	ASTEROID_GROUP,

	/**
	 * Special type of small celestial object which has highly eccentric orbit,
	 * and emits lots of its own mass to space. This emission is mainly caused
	 * by solar(or stellar) wind.
	 */
	COMET,

	/**
	 * Background deep sky object, such as nebula and clusters.
	 */
	DEEPSKYOBJECT;
}