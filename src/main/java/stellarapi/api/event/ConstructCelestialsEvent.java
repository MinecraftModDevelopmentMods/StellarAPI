package stellarapi.api.event;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.world.World;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;

/**
 * Fired to construct the set of celestial collections. <p>
 * Celestial collections and light sources should be registered here. <p>
 * Note that both should be registered every time when this event is called,
 *  and any modification/wrapper should be applied in this event. <p>
 *  
 * This event is <code>@Cancelable</code>,
 * and canceling this event will force the celestial set not to be constructed now.
 * */
@Cancelable
public class ConstructCelestialsEvent extends PerWorldEvent {
	private final List<ICelestialCollection> collections = Lists.newArrayList();
	private final Map<IEffectorType, List<ICelestialObject>> effectorMap = Maps.newHashMap();
	
	public ConstructCelestialsEvent(World world) {
		super(world);
	}
	
	/**
	 * Getter for collections. Register the collections here.
	 * */
	public List<ICelestialCollection> getCollections() {
		return this.collections;
	}
	
	/**
	 * Getter for light sources. Register the effectors here. (The objects should not be temporal)
	 * */
	public List<ICelestialObject> getEffectors(IEffectorType type) {
		if(!effectorMap.containsKey(type))
			effectorMap.put(type, Lists.<ICelestialObject>newArrayList());
		return effectorMap.get(type);
	}

	/**
	 * Gets registered types of effector.
	 * */
	public ImmutableSet<IEffectorType> getEffectorTypes() {
		for(IEffectorType type : effectorMap.keySet())
			if(effectorMap.get(type).isEmpty())
				effectorMap.remove(type);
		
		return ImmutableSet.copyOf(effectorMap.keySet());
	}
}
