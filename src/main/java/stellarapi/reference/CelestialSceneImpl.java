package stellarapi.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.world.World;
import stellarapi.api.SAPIReferences;

public class CelestialSceneImpl implements ICelestialUniverse {

	/**
	 * Tries to construct celestial scene. If it fails, returns null.
	 * */
	public static CelestialSceneImpl constructCelestialScene(World world) {
		ConstructCelestialsEvent construct = new ConstructCelestialsEvent(world);
		if (SAPIReferences.getEventBus().post(construct))
			return null;

		ImmutableSet<IEffectorType> effectorTypes = construct.getEffectorTypes();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();
		for (IEffectorType type : effectorTypes)
			effectors.create(type, construct.getEffectors(type));

		SortCelestialsEvent sort = new SortCelestialsEvent(world, collectionOrdering, construct.getCollections(),
				effectors);
		SAPIReferences.getEventBus().post(sort);

		CelestialCollectionManager collectionManager =
				new CelestialCollectionManager(sort.getSortedCollections());

		HashMap<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();
		for (IEffectorType type : effectorTypes)
			effectorMap.create(type, new CelestialEffectors(sort.getSortedEffectors(type)));

		return new CelestialSceneImpl(collectionManager, effectorMap);
	}

}
