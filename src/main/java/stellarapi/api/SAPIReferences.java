package stellarapi.api;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.daywake.DaytimeChecker;
import stellarapi.api.daywake.SleepWakeManager;
import stellarapi.api.perdimres.IPerDimensionResourceHandler;
import stellarapi.api.perdimres.PerDimensionResourceManager;
import worldsets.api.IReference;

/**
 * Central reference for Stellar API.
 */
public enum SAPIReferences {
	@Deprecated
	INSTANCE;
	
	// ********************************************* //
	// ************** Mod Information ************** //
	// ********************************************* //

	public static final String modid = "stellarapi";
	public static final String version = "@STVERSION@";
	public static final String apiid = "stellarapi|api";

	// ********************************************* //
	// ************** Internal Fields ************** //
	// ********************************************* //

	private static IReference reference;

	private DaytimeChecker dayTimeChecker = new DaytimeChecker();
	private SleepWakeManager sleepWakeManager = new SleepWakeManager();

	private PerDimensionResourceManager resourceManager = new PerDimensionResourceManager();

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


	public static boolean isOpticalEntity(Entity entity) {
		return reference.getPerEntityReference(entity) != null;
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
		World world = reference.getPerClientReference().getClientWorld();
		if (world != null)
			return INSTANCE.resourceManager.getLocation(world, resourceId, defaultLocation);
		else
			return defaultLocation;
	}

	/** For internal use */
	@Deprecated
	public void setReference(IReference base) {
		reference = base;
	}
}
