package stellarapi.reference;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.world.World;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.ICelestialUniverse;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.event.ConstructCelestialsEvent;
import stellarapi.api.event.SortCelestialsEvent;

public class CelestialSceneImpl implements ICelestialUniverse {

	private CelestialCollectionManager collectionManager = null;
	private HashMap<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();

	private static final Ordering<ICelestialCollection> collectionOrdering = Ordering
			.from(new Comparator<ICelestialCollection>() {
				@Override
				public int compare(ICelestialCollection col1, ICelestialCollection col2) {
					return Integer.compare(-col1.searchOrder(), -col2.searchOrder());
				}
			});

	/**
	 * Tries to construct celestial scene. If it fails, returns null.
	 * */
	public static CelestialSceneImpl constructCelestialScene(World world) {
		ConstructCelestialsEvent construct = new ConstructCelestialsEvent(world);
		if (StellarAPIReference.getEventBus().post(construct))
			return null;

		ImmutableSet<IEffectorType> effectorTypes = construct.getEffectorTypes();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();
		for (IEffectorType type : effectorTypes)
			effectors.put(type, construct.getEffectors(type));

		SortCelestialsEvent sort = new SortCelestialsEvent(world, collectionOrdering, construct.getCollections(),
				effectors);
		StellarAPIReference.getEventBus().post(sort);

		CelestialCollectionManager collectionManager =
				new CelestialCollectionManager(sort.getSortedCollections());

		HashMap<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();
		for (IEffectorType type : effectorTypes)
			effectorMap.put(type, new CelestialEffectors(sort.getSortedEffectors(type)));

		return new CelestialSceneImpl(collectionManager, effectorMap);
	}

	CelestialSceneImpl(CelestialCollectionManager manager, HashMap<IEffectorType, CelestialEffectors> effectorMap) {
		this.collectionManager = manager;
		this.effectorMap = effectorMap;
	}

	@Override
	public ImmutableSet<IEffectorType> getEffectorTypeSet() {
		return ImmutableSet.copyOf(effectorMap.keySet());
	}

	@Override
	public CelestialEffectors getCelestialEffectors(IEffectorType type) {
		return effectorMap.get(type);
	}

	@Override
	public CelestialCollectionManager getCollectionManager() {
		return this.collectionManager;
	}

}
