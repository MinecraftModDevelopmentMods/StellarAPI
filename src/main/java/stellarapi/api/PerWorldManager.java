package stellarapi.api;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.event.ConstructCelestialsEvent;
import stellarapi.api.event.ResetCoordinateEvent;
import stellarapi.api.event.ResetSkyEffectEvent;
import stellarapi.api.event.SortCelestialsEvent;

/**
 * Per world manager to contain the per-world(dimension) objects.
 * */
public class PerWorldManager extends WorldSavedData {
	
	private static final String ID = "stellarapiperworldmanager";
	
	private World world;
	
	private CelestialCollectionManager collectionManager = null;
	private HashMap<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();
	private boolean isCelestialsEstablished = false;
	
	private ICelestialCoordinate coordinate;
	private boolean isCoordinateEstablished = false;
	
	private ISkyEffect skyEffect;
	private boolean isSkyEffectEstablished = false;
	
	private Map<String, Object> perWorldData = Maps.newHashMap();
	
	public static void initiatePerWorldManager(World world) {
		world.perWorldStorage.setData(ID, new PerWorldManager(world));
	}
	
	public static PerWorldManager getPerWorldManager(World world) {
		return (PerWorldManager) world.perWorldStorage.loadData(PerWorldManager.class, ID);
	}
	
	private PerWorldManager(World world) {
		super(ID);
		this.world = world;
	}
	
	
	private static final Ordering<ICelestialCollection> collectionOrdering = Ordering.from(
			new Comparator<ICelestialCollection>() {
				@Override
				public int compare(ICelestialCollection col1, ICelestialCollection col2) {
					return Integer.compare(-col1.searchOrder(), -col2.searchOrder());
				}
			});
		
	public void constructCollections() {
		ConstructCelestialsEvent construct = new ConstructCelestialsEvent(this.world);
		if(StellarAPIReference.getEventBus().post(construct))
			return;
		
		ImmutableSet<IEffectorType> effectorTypes = construct.getEffectorTypes();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();
		for(IEffectorType type : effectorTypes)
			effectors.put(type, construct.getEffectors(type));
		
		SortCelestialsEvent sort = new SortCelestialsEvent(this.world,
				collectionOrdering, construct.getCollections(), effectors);
		StellarAPIReference.getEventBus().post(sort);
		
		this.collectionManager = new CelestialCollectionManager(sort.getSortedCollections());
		
		for(IEffectorType type : effectorTypes)
			effectorMap.put(type, new CelestialEffectors(sort.getSortedEffectors(type)));
		
		this.isCelestialsEstablished = true;
	}
	
	public void resetCoordinate() {
		ResetCoordinateEvent coord = new ResetCoordinateEvent(this.world);
		if(StellarAPIReference.getEventBus().post(coord))
			return;
		this.coordinate = coord.getCoordinate();
		this.isCoordinateEstablished = true;
	}
	
	public void resetSkyEffect() {
		ResetSkyEffectEvent sky = new ResetSkyEffectEvent(this.world);
		if(StellarAPIReference.getEventBus().post(sky))
			return;
		this.skyEffect = sky.getSkyEffect();
		this.isSkyEffectEstablished = true;
	}
	
	public CelestialCollectionManager getCollectionManager() {
		if(!this.isCelestialsEstablished)
			this.constructCollections();
		return this.collectionManager;
	}
	
	public CelestialEffectors getCelestialEffectors(IEffectorType type) {
		if(!this.isCelestialsEstablished)
			this.constructCollections();
		return effectorMap.get(type);
	}
	
	public ImmutableSet<IEffectorType> getEffectorTypeSet() {
		if(!this.isCelestialsEstablished)
			this.constructCollections();
		return ImmutableSet.copyOf(effectorMap.keySet());
	}
	
	public ICelestialCoordinate getCoordinate() {
		if(!this.isCoordinateEstablished)
			this.resetCoordinate();
		return this.coordinate;
	}
	
	public ISkyEffect getSkyEffect() {
		if(!this.isSkyEffectEstablished)
			this.resetSkyEffect();
		return this.skyEffect;
	}
	
	/**
	 * Stores the per-world data.
	 * @param id the id
	 * @param data the data
	 * */
	public <T> void storePerWorldData(String id, T data) {
		perWorldData.put(id, data);
	}
	
	/**
	 * Gets the per-world data.
	 * @param id the id
	 * */
	public <T> T getPerWorldData(String id) {
		return (T) perWorldData.get(id);
	}
	
	/**
	 * Removes the per-world data.
	 * @param id the id
	 * */
	public void removePerWorldData(String id) {
		perWorldData.remove(id);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) { }

	@Override
	public void writeToNBT(NBTTagCompound compound) { }

}
