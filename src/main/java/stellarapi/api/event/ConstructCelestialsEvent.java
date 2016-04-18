package stellarapi.api.event;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.world.World;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;

/**
 * Fired to construct the set of celestial collections. <p>
 * Celestial collections and light sources should be registered here. <p>
 * Note that both should be registered every time when this event is called,
 *  and any modification/wrapper should be applied in this event.
 * */
public class ConstructCelestialsEvent extends PerWorldEvent {
	private final List<ICelestialCollection> collections = Lists.newArrayList();
	private final List<ICelestialObject> lightSources = Lists.newArrayList();
	
	public ConstructCelestialsEvent(World world) {
		super(world);
	}
	
	/**
	 * Getter for collections.
	 * */
	public List<ICelestialCollection> getCollections() {
		return this.collections;
	}
	
	/**
	 * Getter for light sources.
	 * */
	public List<ICelestialObject> getLightSources() {
		return this.lightSources;
	}
}
