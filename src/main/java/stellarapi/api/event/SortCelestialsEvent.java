package stellarapi.api.event;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.world.World;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;

/**
 * Fired to define the {@link Ordering} of the light sources,
 *  and default {@link Ordering} of celestial collections. <p>
 * Note that the default ordering of light sources is defined by standard magnitude,
 * and that of celestial collections is defined by priority.
 * */
public class SortCelestialsEvent extends PerWorldEvent {

	private final Map<IEffectorType, Ordering<ICelestialObject>> orderingEffectors;
	private Ordering<ICelestialCollection> orderingCollections;
	
	private final Map<IEffectorType, ImmutableList<ICelestialObject>> effectorSources;
	private final ImmutableList<ICelestialCollection> collections;
	
	public SortCelestialsEvent(World world,
			Ordering<ICelestialCollection> defaultOrderingCC,
			List<ICelestialCollection> collections,
			Map<IEffectorType, List<ICelestialObject>> mapEffectors) {
		super(world);
		this.orderingCollections = defaultOrderingCC;
		this.collections = ImmutableList.copyOf(collections);
		
		this.orderingEffectors = Maps.newHashMap();
		this.effectorSources = Maps.newHashMap();
		for(Map.Entry<IEffectorType, List<ICelestialObject>> entry : mapEffectors.entrySet()) {
			effectorSources.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));
			orderingEffectors.put(entry.getKey(), entry.getKey().getOrderingFor(entry.getValue()));
		}
	}
	
	public void setCollectionsOrdering(Ordering<ICelestialCollection> ordering) {
		this.orderingCollections = ordering;
	}
	
	public Ordering<ICelestialCollection> getCollectionsOrdering() {
		return this.orderingCollections;
	}
	
	public void setEffectorOrdering(IEffectorType type, Ordering<ICelestialObject> ordering) {
		orderingEffectors.put(type, ordering);
	}
	
	public Ordering<ICelestialObject> getEffectorOrdering(IEffectorType type) {
		return orderingEffectors.get(type);
	}
	
	public ImmutableList<ICelestialCollection> getCollections() {
		return this.collections;
	}
	
	public ImmutableList<ICelestialObject> getEffectors(IEffectorType type) {
		return effectorSources.get(type);
	}
	
	public ImmutableList<ICelestialCollection> getSortedCollections() {
		return orderingCollections.immutableSortedCopy(this.collections);
	}
	
	public ImmutableList<ICelestialObject> getSortedEffectors(IEffectorType type) {
		return orderingEffectors.get(type).immutableSortedCopy(effectorSources.get(type));
	}
}
