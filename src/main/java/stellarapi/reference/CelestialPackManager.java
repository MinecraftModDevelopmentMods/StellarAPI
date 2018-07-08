package stellarapi.reference;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import stellarapi.StellarAPI;
import stellarapi.api.SAPIReferences;
import stellarapi.api.celestials.CelestialCollection;
import stellarapi.api.celestials.CelestialCollections;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.helper.WorldProviderReplaceHelper;
import stellarapi.api.pack.ICelestialPack;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.render.IAdaptiveRenderer;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.world.ICelestialHelper;
import stellarapi.api.world.ICelestialWorld;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.api.world.worldset.WorldSets;
import stellarapi.feature.network.MessageSyncPackSettings;
import stellarapi.impl.celestial.DefaultCelestialPack;

/**
 * Per world manager to contain the per-world(dimension) objects.
 */
public class CelestialPackManager implements ICelestialWorld, INBTSerializable<NBTTagCompound> {
	private World world;
	private WorldSet worldSet;
	private ICelestialPack pack;
	private ICelestialScene scene;

	private CelestialCollections collectionManager = null;
	private Map<IEffectorType, CelestialEffectors> effectorMap = Maps.newHashMap();

	private ICCoordinates coordinate;

	private IAtmosphereEffect skyEffect;

	private @Nullable IAdaptiveRenderer renderer;

	CelestialPackManager(World world) {
		this.world = world;

		if(!world.isRemote) {
			// By default, load from configuration on the server.
			this.loadPackFromConfig();
		} else {
			// By default, load with default pack on the client.
			this.worldSet = WorldSets.getPrimaryWorldSet(world);
			this.loadPack(DefaultCelestialPack.INSTANCE, true);
		}
	}

	public void onLackServerAPI() {
		// Load pack from configuration when there's no API.
		this.loadPackFromConfig();
		this.setupWorld();
	}

	public boolean loadPackFromConfig() {
		for(WorldSet wSet : WorldSets.appliedWorldSets(this.world)) {
			// Only one pack for WorldSet for now
			ICelestialPack pack = SAPIReferences.getCelestialPack(wSet);
			if(pack != null) {
				this.worldSet = wSet;
				return this.loadPack(pack, false);
			}
		}
		return true;
	}

	public boolean loadPack(ICelestialPack pack, boolean isDefault) {
		// Load pack without data. This falls back to default or loads configuration.
		this.pack = pack;
		this.scene = pack.getScene(this.worldSet, this.world, isDefault);
		return this.loadPack(this.pack, this.scene);
	}

	public boolean loadPackWithData(ICelestialPack pack, NBTTagCompound data) {
		// Load pack with data.
		this.pack = pack;
		// Load with configuration settings, as it'll be overwritten anyway.
		this.scene = pack.getScene(this.worldSet, this.world, false);
		scene.deserializeNBT(data);
		return this.loadPack(this.pack, this.scene);
	}

	private boolean loadPack(ICelestialPack pack, ICelestialScene scene) {
		// TODO Code defensively; Don't make exceptions here
		List<CelestialCollection> collections = Lists.newArrayList();
		Map<IEffectorType, List<CelestialObject>> effectors = Maps.newHashMap();

		try {
			scene.prepare();
			scene.onRegisterCollection(collection -> collections.add(collection),
					(effType, object) -> effectors.computeIfAbsent(effType, type -> Lists.newArrayList())
					.add(object));

			Collections.sort(collections, collectionOrdering);

			this.collectionManager = new CelestialCollections(collections);
			this.effectorMap = effectors.entrySet().stream().collect(
					Collectors.toMap(entry -> entry.getKey(),
							entry -> new CelestialEffectors(entry.getValue())));
			this.coordinate = scene.createCoordinates();
			this.skyEffect = scene.createAtmosphereEffect();

			ICelestialHelper helper = scene.createCelestialHelper();
			if(helper != null)
				WorldProviderReplaceHelper.patchWorldProviderWith(this.world,
						SAPIReferences.getReplacedWorldProvider(this.world, world.provider, helper));
		} catch(RuntimeException exception) {
			StellarAPI.INSTANCE.getLogger().error("Exception Occured while loading pack", exception);
			return false;
		}

		return true;
	}

	private static final Ordering<CelestialCollection> collectionOrdering = Ordering
			.from(new Comparator<CelestialCollection>() {
				@Override
				public int compare(CelestialCollection col1, CelestialCollection col2) {
					return Integer.compare(-col1.searchOrder(), -col2.searchOrder());
				}
			});

	public IMessage getSyncMessage() {
		if(this.pack != null)
			return new MessageSyncPackSettings(pack.getPackName(), this.scene);
		else return null;
	}

	public void setupWorld() {
		// Sets up the world after everything is decided
		this.renderer = scene.createSkyRenderer();
	}

	@Override
	public CelestialCollections getCollections() {
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
	public ICCoordinates getCoordinate() {
		return this.coordinate;
	}

	@Override
	public IAtmosphereEffect getSkyEffect() {
		return this.skyEffect;
	}

	public @Nullable IAdaptiveRenderer getRenderer() {
		return this.renderer;
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
		// Read pack name from nbt if not forced.
		ICelestialPack readPack = SAPIReferences.getPackWithName(nbt.getString("PackName"));
		if(!StellarAPI.INSTANCE.getPackCfgHandler().forceConfig()) {
			// Select saved pack and load it if it's not forced.
			if(readPack != null)
				this.loadPackWithData(readPack, nbt);
		}
		// When the pack is just missing, go for the one loaded from configuration.
	}

	public void readFromPacket(String packName, NBTTagCompound nbt) {
		ICelestialPack readPack = SAPIReferences.getPackWithName(packName);

		// On the client, read from nbt only if there's the pack with the name.
		// Configuration can overwrite default.
		if(readPack == DefaultCelestialPack.INSTANCE)
			// Load from configuration when it's default
			this.loadPackFromConfig();
		else if(readPack != null)
			// Load from read pack when it exists
			this.loadPackWithData(readPack, nbt);
	}
}
