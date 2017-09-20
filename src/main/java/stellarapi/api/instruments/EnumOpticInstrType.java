package stellarapi.api.instruments;

public enum EnumOpticInstrType {
	/**
	 * Those which converts parallel rays into another parallel rays.
	 * */
	SCOPE,

	/**
	 * Those which converts parallel rays into focused rays.
	 * */
	FOCUS,

	/**
	 * Those which converts focused rays into another focused rays.
	 * */
	PROPAGATE,

	/**
	 * Those which converts focuesd rays into parallel rays.
	 * */
	DISPERSE;
}