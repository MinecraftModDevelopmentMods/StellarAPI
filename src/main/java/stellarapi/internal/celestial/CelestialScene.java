package stellarapi.internal.celestial;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import stellarapi.api.celestials.CelestialLayer;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.ICelestialScene;
import stellarapi.api.celestials.ICelestialSystem;
import stellarapi.api.celestials.collection.ICollectionAdaption;

public class CelestialScene implements ICelestialScene {

	private Map<RegistryDelegate<CelestialType>, ICollectionAdaption> adaptionMap = Maps.newHashMap();
	private List<CelestialLayer> layers = Lists.newArrayList();

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

}
