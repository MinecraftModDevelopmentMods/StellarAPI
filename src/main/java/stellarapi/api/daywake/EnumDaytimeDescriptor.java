package stellarapi.api.daywake;

/**
 * Daytime descriptor to note the time for day.
 * <p>
 * For minecraft compatibility.
 */
public enum EnumDaytimeDescriptor {
	/**
	 * Midnight, usually when main light source or 'Sun' is directly under the
	 * ground (on Nadir), generally when sum of celestial light sources is
	 * darkest.
	 */
	MIDNIGHT,

	/**
	 * Midday, usually when main light source or 'Sun' is directly over the sky
	 * (on Zenith). generally when sum of celestial light source is brightest.
	 */
	MIDDAY,

	/**
	 * Early Morning, when it is dark enough to call it 'night', after midnight.
	 */
	EARLY_MORNING,

	/**
	 * Morning, when it is bright enough to call it 'day', before midday.
	 */
	MORNING,

	/**
	 * Afternoon, when it is bright enough to call it 'day', after midday.
	 */
	AFTERNOON,

	/**
	 * Evening, when it is dark enough to call it 'night', before midnight.
	 */
	EVENING,

	/**
	 * Dawn, usually when main light source or 'Sun' is rising.
	 */
	DAWN,

	/**
	 * Dusk, usually when main light source or 'Sun' is setting.
	 */
	DUSK
}
