package stellarapi.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import stellarapi.api.celestials.CelestialCollections;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.event.FOVEvent;
import stellarapi.api.event.QEEvent;
import stellarapi.api.optics.Wavelength;
import stellarapi.api.pack.ICelestialPack;
import stellarapi.api.pack.ICelestialScene;
import stellarapi.api.view.IAtmosphereEffect;
import stellarapi.api.view.ICCoordinates;
import stellarapi.api.world.ICelestialHelper;
import stellarapi.api.world.ICelestialWorld;
import stellarapi.api.world.IWorldProviderReplacer;
import stellarapi.api.world.resource.IWorldResourceHandler;
import stellarapi.api.world.resource.WorldResourceManager;
import stellarapi.api.world.worldset.WorldSet;

/**
 * <p>Central reference for Stellar API.</p>
 * <p>There are other reference points in Stellar API :</p>
 * <ul>
 * <li>{@link stellarapi.api.world.worldset.WorldSets WorldSets} for worldsets</li>
 * </ul>
 */
public final class SAPIReferences {

	// ********************************************* //
	// ************** Mod Information ************** //
	// ********************************************* //
	public static final String MODID = "stellarapi";
	public static final String VERSION = "@STVERSION@";
	public static final String APIID = "stellarapi|api";

	// ********************************************* //
	// *********** StellarAPI References *********** //
	// ********************************************* //

	private Map<WorldSet, ICelestialPack> linkedPacks = Maps.newIdentityHashMap();
	private Map<String, ICelestialPack> nameToPacks = Maps.newHashMap();
	private List<IWorldProviderReplacer> worldProvReplacers = Lists.newArrayList();
	private DaytimeChecker dayTimeChecker = new DaytimeChecker();
	private SleepWakeManager sleepWakeManager = new SleepWakeManager();

	private WorldResourceManager resourceManager = new WorldResourceManager();

	private static final SAPIReferences INSTANCE = new SAPIReferences();

	/** Getter for the daytime checker. */
	public static DaytimeChecker getDaytimeChecker() {
		return INSTANCE.dayTimeChecker;
	}

	/** Getter for sleep wake manager */
	public static SleepWakeManager getSleepWakeManager() {
		return INSTANCE.sleepWakeManager;
	}


	/** Registers the pack. Placeholder method before 1.13 data packs. */
	public static void registerPack(ICelestialPack pack) {
		INSTANCE.nameToPacks.put(pack.getPackName(), pack);
	}

	/** Gets the celestial pack with name. */
	public static ICelestialPack getPackWithName(String packName) {
		return INSTANCE.nameToPacks.get(packName);
	}


	/** Placeholder method for versions before 1.13(data packs) - put this in any time */
	public static void setCelestialPack(WorldSet worldSet, ICelestialPack pack) {
		INSTANCE.linkedPacks.put(worldSet, pack);
	}

	/** Gets the celestial pack for certain WorldSet. */
	public static ICelestialPack getCelestialPack(WorldSet worldSet) {
		return INSTANCE.linkedPacks.get(worldSet);
	}

	/** Gets the active scene for now. It will be removed. */
	@Deprecated
	public static @Nullable ICelestialScene getActivePack(World world) {
		return reference.getActivePack(world);
	}


	/**
	 * Registers world provider replacer.
	 * @param replacer the world provider replacer to register
	 * */
	public static void registerWorldProviderReplacer(IWorldProviderReplacer replacer) {
		INSTANCE.worldProvReplacers.add(replacer);
	}

	/**
	 * Gets replaced world provider.
	 * @param world the world to replace the provider
	 * @param originalProvider original provider to be replaced
	 * @param helper the celestial helper
	 * @return the provider which will replace original provider
	 * */
	public static WorldProvider getReplacedWorldProvider(World world, WorldProvider originalProvider, ICelestialHelper helper) {
		for(IWorldProviderReplacer replacer : INSTANCE.worldProvReplacers)
			if(replacer.accept(world, originalProvider))
				return replacer.createWorldProvider(world, originalProvider, helper);

		return reference.getDefaultReplacer().createWorldProvider(world, originalProvider, helper);
	}


	/**
	 * Checks if this world is default.
	 * On server, this checks for overworld.
	 * On client, this checks for the main loaded world.
	 * */
	public static boolean isDefaultWorld(World world) {
		if(world.isRemote)
			return true;
		return world.provider.getDimension() == 0;
	}

	/**
	 * Get one of the default worlds.
	 * */
	public static World getDefaultWorld(boolean isRemote) {
		return reference.getDefaultWorld(isRemote);
	}

	/**
	 * registers per dimension resource handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public static void registerPerDimResourceHandler(IWorldResourceHandler handler) {
		INSTANCE.resourceManager.register(handler);
	}

	/**
	 * Estimates FOV of an entity on server.
	 * */
	public static float estimateFOV(Entity entity) {
		float mult = 1.0f;
		if(entity instanceof EntityPlayer) {
			FOVUpdateEvent updateEvent = new FOVUpdateEvent((EntityPlayer)entity, 1.0f);
			MinecraftForge.EVENT_BUS.post(updateEvent);
			mult = updateEvent.getNewfov();
		}
		FOVEvent event = new FOVEvent(entity, mult * 70.0f);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getFOV();
	}

	/**
	 * Estimates Quantum Efficiency of an entity on server.
	 * */
	public static float estimateQE(Entity entity, Wavelength wavelength) {
		QEEvent event = new QEEvent(entity, wavelength, 1.0f);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getQE();
	}

	/**
	 * Gets celestial coordinate for certain world.
	 * 
	 * @param world
	 *            the world
	 * @return the coordinate for the world if it is available now, or
	 *         <code>null</code> otherwise
	 */
	@Deprecated
	public static ICCoordinates getCoordinate(World world) {
		ICelestialWorld worldRef = reference.getCelestialWorld(world);
		return worldRef != null? worldRef.getCoordinate() : null;
	}

	/**
	 * Gets sky effect for certain world.
	 * 
	 * @param world
	 *            the world
	 * @return the sky effect for the world if it is available now, or
	 *         <code>null</code> otherwise
	 */
	@Deprecated
	public static IAtmosphereEffect getAtmosphereEffect(World world) {
		ICelestialWorld worldRef = reference.getCelestialWorld(world);
		return worldRef != null? worldRef.getSkyEffect() : null;
	}

	/**
	 * Gets set of types of celestial effectors for certain world.
	 * 
	 * @param world
	 *            the world
	 * @return the immutable set with effect types on the world
	 */
	@Deprecated
	public static ImmutableSet<IEffectorType> getEffectTypeSet(World world) {
		ICelestialWorld worldRef = reference.getCelestialWorld(world);
		return worldRef != null? worldRef.getEffectorTypeSet() : null;
	}

	/**
	 * Gets celestial effectors for certain world.
	 * <p>
	 * There are light sources(or Sun), tidal effectors(or Moon), and so on.
	 * 
	 * @param world
	 *            the world
	 * @param type
	 *            the celestial effector type
	 * @return the celestial effectors for the world if it exists, or
	 *         <code>null</code> otherwise
	 */
	@Deprecated
	public static CelestialEffectors getEffectors(World world, IEffectorType type) {
		ICelestialWorld worldRef = reference.getCelestialWorld(world);
		return worldRef != null? worldRef.getCelestialEffectors(type) : null;
	}

	/**
	 * Gets celestial collection manager for certain world.
	 * 
	 * @param world
	 *            the world
	 * @return the celestial collection manager for the world, or
	 *         <code>null</code> if it is not established yet.
	 */
	@Deprecated
	public static CelestialCollections getCollections(World world) {
		ICelestialWorld worldRef = reference.getCelestialWorld(world);
		return worldRef != null? worldRef.getCollections() : null;
	}

	/**
	 * Gets per-dimension resource location for certain resource ID.
	 * <p>
	 * Note that this should only be called on client.
	 * 
	 * @param resourceId
	 *            the resource ID
	 * @param defaultLocation
	 *            the default resource location
	 */
	public static ResourceLocation getLocation(String resourceId, ResourceLocation defaultLocation) {
		World world = reference.getDefaultWorld(true);
		if (world != null)
			return INSTANCE.resourceManager.getLocation(world, resourceId, defaultLocation);
		else
			return defaultLocation;
	}

	// ********************************************* //
	// ****************** Internal ***************** //
	// ********************************************* //

	private static IReference reference;

	@Deprecated
	public static void putReference(IReference base) {
		reference = base;
	}
}
