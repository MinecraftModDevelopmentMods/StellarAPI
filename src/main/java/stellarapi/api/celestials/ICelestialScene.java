package stellarapi.api.celestials;

import com.google.common.collect.ImmutableList;

import stellarapi.api.celestials.collection.ICollectionAdaption;

/**
 * World Capability of celestial objects and the scene of them.
 * */
public interface ICelestialScene {
	/** Sets up this scene */
	public void setupComplete(ICelestialSystem system);

	/** Check if adaption for certain type is absent */
	public boolean isAbsent(CelestialType type);

	/** Gets adaption */
	public ICollectionAdaption getAdaption(CelestialType type);

	/** Gets the layers */
	public ImmutableList<CelestialLayer> getLayers();
}
