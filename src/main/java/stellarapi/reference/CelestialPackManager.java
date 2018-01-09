package stellarapi.reference;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import stellarapi.StellarAPI;
import stellarapi.api.ICelestialCoordinates;
import stellarapi.api.ICelestialPack;
import stellarapi.api.ICelestialScene;
import stellarapi.api.ICelestialWorld;
import stellarapi.api.IPerWorldReference;
import stellarapi.api.ISkyEffect;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.ICelestialCollection;
import stellarapi.api.celestials.ICelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.helper.WorldProviderReplaceHelper;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.feature.network.MessageSyncPackSettings;
import stellarapi.impl.celestial.DefaultCelestialPack;

/**
 * Per world manager to contain the per-world(dimension) objects.
 */
public class CelestialPackManager implements ICelestialWorld, IPerWorldReference, INBTSerializable<NBTTagCompound> {
	private World world;
	private WorldSet worldSet;
	private ICelestialPack pack;
	private ICelestialScene scene;

	private CelestialCollectionManager collectionManager = null;
	private Map<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();

	private ICelestialCoordinates coordinate;

	private ISkyEffect skyEffect;

	CelestialPackManager(World world) {
		this.world = world;

		for(WorldSet wSet : SAPIReferences.appliedWorldSets(world)) {
			// Only one pack for WorldSet for now
			ICelestialPack pack = SAPIReferences.getCelestialPack(wSet);
			if(pack != null) {
				this.worldSet = wSet;
				this.loadPack(pack, false);
				break;
			}
		}
	}

	public void onVanillaServer() {
		this.loadPack(this.pack, true);
	}

	public void loadPack(ICelestialPack pack, boolean vanillaServer) {
		// TODO Find a way to switch from default to custom on some case
		this.pack = pack;
		this.scene = pack.getScene(this.worldSet, this.world, vanillaServer);
		this.loadPack(this.pack, this.scene);
	}

	public void loadPackWithData(ICelestialPack pack, NBTTagCompound data) {
		this.pack = pack;
		this.scene = pack.getScene(this.worldSet, this.world, false);
		scene.deserializeNBT(data);
		this.loadPack(this.pack, this.scene);
	}

	private void loadPack(ICelestialPack pack, ICelestialScene scene) {
		List<ICelestialCollection> collections = Lists.newArrayList();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();

		scene.prepare();
		scene.onRegisterCollection(collection -> collections.add(collection),
				(effType, object) -> effectors.computeIfAbsent(effType, type -> Lists.newArrayList())
				.add(object));

		Collections.sort(collections, collectionOrdering);

		this.collectionManager = new CelestialCollectionManager(collections);
		this.effectorMap = effectors.entrySet().stream().collect(
				Collectors.toMap(entry -> entry.getKey(),
						entry -> new CelestialEffectors(entry.getValue())));
		this.coordinate = scene.createCoordinates();
		this.skyEffect = scene.createSkyEffect();

		WorldProvider replaced = scene.replaceWorldProvider(world.provider);
		if(replaced != null)
			WorldProviderReplaceHelper.patchWorldProviderWith(this.world, replaced);
	}

	private static final Ordering<ICelestialCollection> collectionOrdering = Ordering
			.from(new Comparator<ICelestialCollection>() {
				@Override
				public int compare(ICelestialCollection col1, ICelestialCollection col2) {
					return Integer.compare(-col1.searchOrder(), -col2.searchOrder());
				}
			});

	public IMessage getSyncMessage() {
		if(this.pack != null)
			return new MessageSyncPackSettings(pack.getPackName(), this.scene);
		else return null;
	}

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

	public ICelestialScene getScene() {
		return this.scene;
	}


	@Override
	public NBTTagCompound serializeNBT() {
		if(this.scene != null) {
			NBTTagCompound nbt = scene.serializeNBT();
			nbt.setString("PackName", pack.getPackName());
			return nbt;
		}
		else return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(this.scene != null) {
			if(!StellarAPI.INSTANCE.getPackCfgHandler().forceConfig()) {
				// Select saved pack and load it if it's not forced.
				ICelestialPack pack = SAPIReferences.getPackWithName(nbt.getString("PackName"));
				if(pack != null)
					this.loadPackWithData(pack, nbt);
			}
		}
	}
}
