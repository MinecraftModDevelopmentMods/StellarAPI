package stellarapi.internal.celestial;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraftforge.registries.IRegistryDelegate;
import stellarapi.api.celestials.CelestialLayer;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.celestials.collection.ICollectionAdaption;

public class CelestialScene implements ICelestialScene {

	private ImmutableMap<IRegistryDelegate<CelestialType>, ICollectionAdaption> adaptionMap;
	private ImmutableList<CelestialLayer> layers;

	public CelestialScene() { }

	@Override
	public void setupComplete(ICelestialSystem system) {
		// TODO CelestialScene setup complete
	}

	@Override
	public boolean isAbsent(CelestialType type) {
		return !adaptionMap.containsKey(type.delegate);
	}

	@Override
	public ICollectionAdaption getAdaption(CelestialType type) {
		return adaptionMap.get(type.delegate);
	}

	@Override
	public ImmutableList<CelestialLayer> getLayers() {
		return this.layers;
	}

}