package stellarapi.api.celestials;

import stellarapi.api.celestials.collection.ICollectionAdaption;

public interface ICelestialScene {
	/** Sets up this scene */
	public void setupComplete(ICelestialSystem system);

	/** Check if adaption for certain type is absent */
	public boolean isAbsent(CelestialType type);

	/** Gets adaption */
	public ICollectionAdaption getAdaption(CelestialType type);
}
