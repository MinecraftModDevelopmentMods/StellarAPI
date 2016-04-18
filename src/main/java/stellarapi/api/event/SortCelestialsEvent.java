package stellarapi.api.event;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.minecraft.world.World;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;

/**
 * Fired to define the {@link Ordering} of the light sources,
 *  and default {@link Ordering} of celestial collections. <p>
 * Note that the default ordering of light sources is defined by standard magnitude,
 * and that of celestial collections is defined by priority.
 * */
public class SortCelestialsEvent extends PerWorldEvent {

	private Ordering<ICelestialObject> orderingLightSources;
	private Ordering<ICelestialCollection> orderingCollections;
	
	private ImmutableList<ICelestialObject> lightSources;
	private ImmutableList<ICelestialCollection> collections;
	
	public SortCelestialsEvent(World world, Ordering<ICelestialObject> defaultOrderingLS,
			Ordering<ICelestialCollection> defaultOrderingCC,
			List<ICelestialObject> lightSources,
			List<ICelestialCollection> collections) {
		super(world);
		this.orderingLightSources = defaultOrderingLS;
		this.orderingCollections = defaultOrderingCC;
		this.lightSources = ImmutableList.copyOf(lightSources);
		this.collections = ImmutableList.copyOf(collections);
	}
	
	public void setLightSourceOrdering(Ordering<ICelestialObject> ordering) {
		this.orderingLightSources = ordering;
	}
	
	public Ordering<ICelestialObject> getLightSourceOrdering() {
		return this.orderingLightSources;
	}
	
	public void setCollectionsOrdering(Ordering<ICelestialCollection> ordering) {
		this.orderingCollections = ordering;
	}
	
	public Ordering<ICelestialCollection> getCollectionsOrdering() {
		return this.orderingCollections;
	}
	
	public ImmutableList<ICelestialObject> getLightSources() {
		return this.lightSources;
	}
	
	public ImmutableList<ICelestialCollection> getCollections() {
		return this.collections;
	}
	
	public ImmutableList<ICelestialObject> getSortedLightSources() {
		return orderingLightSources.immutableSortedCopy(this.lightSources);
	}
	
	public ImmutableList<ICelestialCollection> getSortedCollections() {
		return orderingCollections.immutableSortedCopy(this.collections);
	}
}
