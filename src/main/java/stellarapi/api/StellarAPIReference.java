package stellarapi.api;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import stellarapi.StellarAPI;
import stellarapi.api.celestials.CelestialCollectionManager;
import stellarapi.api.celestials.CelestialLightSources;
import stellarapi.api.mc.DaytimeChecker;
import stellarapi.api.mc.SleepWakeManager;
import stellarapi.api.perdimres.IPerDimensionResourceHandler;
import stellarapi.api.perdimres.PerDimensionResourceManager;

/**
 * Central reference for Stellar API.
 * */
public final class StellarAPIReference {
		
	private DaytimeChecker dayTimeChecker = new DaytimeChecker();
	private SleepWakeManager sleepWakeManager = new SleepWakeManager();
	
	private PerDimensionResourceManager resourceManager = new PerDimensionResourceManager();
	
	private EventBus stellarEventBus = new EventBus();
	
	private static StellarAPIReference INSTANCE = new StellarAPIReference();
	
	
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
	 * @param handler the handler
	 * */
	public static void registerPerDimResourceHandler(IPerDimensionResourceHandler handler) {
		INSTANCE.resourceManager.register(handler);
	}
	
	
	/**
	 * Constructs the celestial collections/objects for the world.
	 * It is necessary to call this method at least once per world with celestial settings.
	 * */
	public static void constructCelestials(World world) {
		PerWorldManager.getPerWorldManager(world).constructCollections();
	}

	/**
	 * Resets the celestial coordinate for the world.
	 * It is necessary to call this method at least once per world with celestial settings.
	 * */
	public static void resetCoordinate(World world) {
		PerWorldManager.getPerWorldManager(world).resetCoordinate();
	}
	
	/**
	 * Resets the sky effect for the world.
	 * It is necessary to call this method at least once per world with celestial settings.
	 * */
	public static void resetSkyEffect(World world) {
		PerWorldManager.getPerWorldManager(world).resetSkyEffect();
	}
	
	
	/**
	 * Gets the event bus for Stellar API.
	 * */
	public static EventBus getEventBus() {
		return INSTANCE.stellarEventBus;
	}
	
	
	/**
	 * Gets celestial coordinate for certain world.
	 * @param world the world
	 * @return the coordinate for the world if it exists, or <code>null</code> otherwise
	 * */
	public static ICelestialCoordinate getCoordinate(World world) {
		return PerWorldManager.getPerWorldManager(world).getCoordinate();
	}
	
	/**
	 * Gets sky effect for certain world.
	 * @param world the world
	 * @return the sky effect for the world if it exists, or <code>null</code> otherwise
	 * */
	public static ISkyEffect getSkyEffect(World world) {
		return PerWorldManager.getPerWorldManager(world).getSkyEffect();
	}
	
	/**
	 * Gets celestial light sources for certain world.
	 * @param world the world
	 * @return the light sources for the world if it exists, or <code>null</code> otherwise
	 * */
	public static CelestialLightSources getLightSources(World world) {
		return PerWorldManager.getPerWorldManager(world).getCelestialLightSources();
	}
	
	/**
	 * Gets celestial collection manager for certian world.
	 * @param world the world
	 * @return the light sources for the world if it exists, or <code>null</code> otherwise
	 * */
	public static CelestialCollectionManager getCollectionManager(World world) {
		return PerWorldManager.getPerWorldManager(world).getCollectionManager();
	}
	
	
	/**
	 * Gets per-dimension resource location for certain resource ID. <p>
	 * Note that this should only be called on client.
	 * @param resourceId the resource ID
	 * @param defaultLocation the default resource location
	 * */
	public static ResourceLocation getLocation(String resourceId, ResourceLocation defaultLocation) {
		World world = StellarAPI.proxy.getDefWorld();
		if(world != null)
			return INSTANCE.resourceManager.getLocation(world, resourceId, defaultLocation);
		else return defaultLocation;
	}
}
