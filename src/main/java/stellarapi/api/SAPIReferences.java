package stellarapi.api;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialEffectors;
import stellarapi.api.celestials.IEffectorType;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.optics.IOpticalFilter;
import stellarapi.api.optics.IViewScope;
import stellarapi.api.perdimres.IPerDimensionResourceHandler;
import stellarapi.api.perdimres.PerDimensionResourceManager;
import stellarapi.api.world.worldset.WorldSet;

/**
 * Central reference for Stellar API.
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

	private Map<WorldSet, ICelestialPack> packs = Maps.newIdentityHashMap();
	private Map<String, ICelestialPack> nameToPacks = Maps.newHashMap();
	private DaytimeChecker dayTimeChecker = new DaytimeChecker();
	private SleepWakeManager sleepWakeManager = new SleepWakeManager();

	private PerDimensionResourceManager resourceManager = new PerDimensionResourceManager();

	private EventBus stellarEventBus = new EventBus();

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
		INSTANCE.packs.put(worldSet, pack);
	}

	/** Gets the celestial pack for certain WorldSet. */
	public static ICelestialPack getCelestialPack(WorldSet worldSet) {
		return INSTANCE.packs.get(worldSet);
	}

	/** Gets the active scene for now. */
	public static @Nullable ICelestialScene getActivePack(World world) {
		return reference.getActivePack(world);
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

	public static final ResourceLocation VANILLA_FACTORY = new ResourceLocation("basics");
	public static final ResourceLocation NAMED_WORLDSET_FACTORY = new ResourceLocation("named");

	/** Can only be used after Init phase of this API. */
	public static WorldSet exactOverworld() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[0];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet overworldType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[1];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet endType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[2];
	}

	/** Can only be used after Init phase of this API. */
	public static WorldSet netherType() {
		return reference.getGeneratedWorldSets(VANILLA_FACTORY)[3];
	}

	/**
	 * Gets the list of the world sets.
	 * */
	public static ImmutableList<WorldSet> getAllWorldSets() {
		return reference.getAllWorldSets();
	}

	/**
	 * Gets primary WorldSet for this world.
	 * */
	public static @Nullable WorldSet getPrimaryWorldSet(World world) {
		return reference.getPrimaryWorldSet(world);
	}

	/**
	 * Gets applied WorldSets for this world. WorldSet with higher priority comes first.
	 * */
	public static ImmutableList<WorldSet> appliedWorldSets(World world) {
		return reference.appliedWorldSets(world);
	}

	/**
	 * registers per dimension resource handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public static void registerPerDimResourceHandler(IPerDimensionResourceHandler handler) {
		INSTANCE.resourceManager.register(handler);
	}

	public static boolean isOpticalEntity(Entity entity) {
		return reference.getPerEntityReference(entity) != null;
	}

	/**
	 * Updates the scope for the entity. Only works for entities with optical
	 * event callback capabilities.
	 * 
	 * @param additionalParams
	 *            additional parameters like changed itemstack.
	 */
	public static void updateScope(Entity entity, Object... additionalParams) {
		reference.getPerEntityReference(entity).updateScope(additionalParams);
	}

	/**
	 * Updates the filter for the entity. Only works for entities with optical
	 * event callback capabilities.
	 * 
	 * @param additionalParams
	 *            additional parameters like changed itemstack.
	 */
	public static void updateFilter(Entity entity, Object... additionalParams) {
		reference.getPerEntityReference(entity).updateFilter(additionalParams);
	}

	/**
	 * Gets the event bus for Stellar API.
	 */
	public static EventBus getEventBus() {
		return INSTANCE.stellarEventBus;
	}

	/**
	 * Gets celestial coordinate for certain world.
	 * <p>
	 * Note that it should always exist, but the result can be <code>null</code>
	 * in the cases the initialization has delayed.
	 * 
	 * @param world
	 *            the world
	 * @return the coordinate for the world if it is available now, or
	 *         <code>null</code> otherwise
	 */
	public static ICelestialCoordinates getCoordinate(World world) {
		return reference.getPerWorldReference(world).getCoordinate();
	}

	/**
	 * Gets sky effect for certain world.
	 * <p>
	 * Note that it should always exist for worlds with sky, but the result can
	 * be <code>null</code> in the cases the initialization has delayed.
	 * 
	 * @param world
	 *            the world
	 * @return the sky effect for the world if it is available now, or
	 *         <code>null</code> otherwise
	 */
	public static ISkyEffect getSkyEffect(World world) {
		return reference.getPerWorldReference(world).getSkyEffect();
	}

	/**
	 * Gets set of types of celestial effectors for certain world.
	 * <p>
	 * 
	 * @param world
	 *            the world
	 * @return the immutable set with effect types on the world
	 */
	public static ImmutableSet<IEffectorType> getEffectTypeSet(World world) {
		return reference.getPerWorldReference(world).getEffectorTypeSet();
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
	public static CelestialEffectors getEffectors(World world, IEffectorType type) {
		return reference.getPerWorldReference(world).getCelestialEffectors(type);
	}

	/**
	 * Gets celestial collection manager for certain world.
	 * 
	 * @param world
	 *            the world
	 * @return the celestial collection manager for the world, or
	 *         <code>null</code> if it is not established yet.
	 */
	public static CelestialCollectionManager getCollectionManager(World world) {
		return reference.getPerWorldReference(world).getCollectionManager();
	}

	/**
	 * Checks if certain entity has optical information.
	 * Check this before trying to get the scope.
	 * @param entity the entity
	 * */
	public static boolean hasOpticalInformation(Entity entity) {
		return reference.getPerEntityReference(entity) != null;
	}

	/**
	 * Gets scope for certain entity.
	 * @param entity the entity
	 */
	public static IViewScope getScope(Entity entity) {
		IPerEntityReference ref = reference.getPerEntityReference(entity);
		return ref != null? ref.getScope() : reference.getDefaultScope();
	}

	/**
	 * Gets filter for certain entity.
	 * @param entity the entity
	 */
	public static IOpticalFilter getFilter(Entity entity) {
		IPerEntityReference ref = reference.getPerEntityReference(entity);
		return ref != null? ref.getFilter() : reference.getDefaultFilter();
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
