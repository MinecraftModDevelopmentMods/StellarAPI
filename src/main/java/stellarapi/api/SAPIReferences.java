package stellarapi.api;

import com.google.common.collect.ImmutableSet;

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

	/**
	 * registers per dimension resource handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public static void registerPerDimResourceHandler(IPerDimensionResourceHandler handler) {
		INSTANCE.resourceManager.register(handler);
	}

	/**
	 * Constructs the celestial collections/objects for the world. It is
	 * necessary to call this method at least once per world with celestial
	 * settings.
	 */
	public static void constructCelestials(World world) {
		reference.getPerWorldReference(world).constructCollections();
	}

	/**
	 * Resets the celestial coordinate for the world. It is necessary to call
	 * this method at least once per world with celestial settings.
	 */
	public static void resetCoordinate(World world) {
		reference.getPerWorldReference(world).resetCoordinate();
	}

	/**
	 * Resets the sky effect for the world. It is necessary to call this method
	 * at least once per world with celestial settings.
	 */
	public static void resetSkyEffect(World world) {
		reference.getPerWorldReference(world).resetSkyEffect();
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
	public static ICelestialCoordinate getCoordinate(World world) {
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
		World world = reference.getClientWorld();
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
