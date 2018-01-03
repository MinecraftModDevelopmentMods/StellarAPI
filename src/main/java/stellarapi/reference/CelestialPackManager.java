package stellarapi.reference;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.ISkyEffect;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import worldsets.api.WAPIReferences;
import worldsets.api.worldset.WorldSet;

/**
 * Per world manager to contain the per-world(dimension) objects.
 */
public class CelestialPackManager implements ICelestialWorld, IPerWorldReference {

	private static final String ID = "stellarapiperworldmanager";

	private World world;

	private CelestialCollectionManager collectionManager = null;
	private Map<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();

	private ICelestialCoordinates coordinate;

	private ISkyEffect skyEffect;

	CelestialPackManager(World world) {
		this.world = world;
		List<ICelestialCollection> collections = Lists.newArrayList();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();

		for(WorldSet wSet : WAPIReferences.appliedWorldSets(world)) {
			// Only one pack for WorldSet for now
			ICelestialPack pack = SAPIReferences.getCelestialPack(wSet);
			if(pack != null) {
				pack.onRegisterCollection(world, collection -> collections.add(collection),
						(effType, object) -> effectors.computeIfAbsent(
								effType, type -> Lists.newArrayList()).add(object));
				this.coordinate = pack.createCoordinates(world);
				this.skyEffect = pack.createSkyEffect(world);
				break;
			}
		}
	}

	private static final Ordering<ICelestialCollection> collectionOrdering = Ordering
			.from(new Comparator<ICelestialCollection>() {
				@Override
				public int compare(ICelestialCollection col1, ICelestialCollection col2) {
					return Integer.compare(-col1.searchOrder(), -col2.searchOrder());
				}
			});


	@Override
	public CelestialCollectionManager getCollectionManager() {
		return this.collectionManager;
	}

	@Override
	public CelestialEffectors getCelestialEffectors(IEffectorType type) {
		return effectorMap.get(type);
	}

	@Override
	public ImmutableSet<IEffectorType> getEffectorTypeSet() {
		return ImmutableSet.copyOf(effectorMap.keySet());
	}

	@Override
	public ICelestialCoordinates getCoordinate() {
		return this.coordinate;
	}

	@Override
	public ISkyEffect getSkyEffect() {
		return this.skyEffect;
	}
}
