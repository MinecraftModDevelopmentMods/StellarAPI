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
import stellarapi.feature.celestial.tweakable.SAPICelestialPack;
import stellarapi.feature.celestial.tweakable.SAPIConfigHandler;
import stellarapi.feature.network.MessageSyncPackSettings;
import stellarapi.impl.celestial.DefaultCelestialPack;

/**
 * Per world manager to contain the per-world(dimension) objects.
 */
public class CelestialPackManager implements ICelestialWorld, IPerWorldReference, INBTSerializable<NBTTagCompound> {

	private static final String ID = "stellarapiperworldmanager";

	private World world;

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
				this.loadPack(pack);
				break;
			}
		}
	}

	public void onVanillaServer() {
		if(this.pack instanceof SAPICelestialPack) {
			this.loadPack(new DefaultCelestialPack());
		}
	}

	public void loadPack(ICelestialPack pack) {
		this.pack = pack;

		List<ICelestialCollection> collections = Lists.newArrayList();
		Map<IEffectorType, List<ICelestialObject>> effectors = Maps.newHashMap();

		this.scene = pack.getScene(this.world);
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
			return new MessageSyncPackSettings(this.pack);
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
		if(this.pack != null) {
			NBTTagCompound nbt = pack.serializeNBT();
			nbt.setString("PackName", pack.getPackName());
			return nbt;
		}
		else return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(this.pack != null) {
			if(pack.getPackName().equals(nbt.getString("PackName"))) {
				if(!SAPIConfigHandler.forceConfig)
					// Load save, which overwrites pack from configuration.
					pack.deserializeNBT(nbt);
			}
		}
	}
}
