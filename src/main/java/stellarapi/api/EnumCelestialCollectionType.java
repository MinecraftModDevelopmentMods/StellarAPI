package stellarapi.api;

public enum EnumCelestialCollectionType {
	/**
	 * Collection of {@linkplain EnumCelestialObjectType#Star stars}.
	 * */
	Stars,
	
	/**
	 * Collection represents certain planetary system.
	 * */
	System,
	
	/**
	 * Collection of {@linkplain EnumCelestialObjectType#Asteroid asteroids}.
	 * */
	Asteroids,
	
	/**
	 * Collection of {@linkplain EnumCelestialObjectType#Comet comets}.
	 * */
	Comets,
	
	/**
	 * Collection of {@linkplain EnumCelestialObjectType#DeepSkyObject deep sky objets}.
	 * */
	DeepSkyObjects;
}
