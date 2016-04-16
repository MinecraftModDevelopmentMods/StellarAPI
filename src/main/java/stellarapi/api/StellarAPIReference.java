package stellarapi.api;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import stellarapi.StellarAPI;
import stellarapi.api.mc.DaytimeChecker;
import stellarapi.api.mc.SleepWakeManager;
import stellarapi.api.perdimension.IPerDimensionResourceHandler;
import stellarapi.api.perdimension.IPerWorldGetter;
import stellarapi.api.perdimension.IntegratedPerWorldGetter;
import stellarapi.api.perdimension.PerDimensionResourceManager;

public final class StellarAPIReference {
	
	private List<ISkyRendererType> rendererTypes = Lists.newArrayList();
	
	private IntegratedPerWorldGetter<ICelestialCoordinate> coordinateGetter = new IntegratedPerWorldGetter();
	private IntegratedPerWorldGetter<ISkyEffect> skyEffectGetter = new IntegratedPerWorldGetter();
	private IntegratedPerWorldGetter<CelestialLightSources> lightSourcesGetter = new IntegratedPerWorldGetter();	
	
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
	 * Registers sky renderer type. <p>
	 * Note that this should be done on both side.
	 * @param rendererType the sky renderer type to register
	 * */
	public static void registerRendererType(ISkyRendererType rendererType) {
		INSTANCE.rendererTypes.add(rendererType);
	}
	
	/**
	 * Registers the celestial coordinate.
	 * @param getter the per-dimension getter for coordinate
	 * */
	public static void registerCoordinate(IPerWorldGetter<ICelestialCoordinate> getter) {
		INSTANCE.coordinateGetter.register(getter);
	}
	
	/**
	 * Registers the sky effect.
	 * @param getter the per-dimension getter for sky effect
	 * */
	public static void registerSkyEffect(IPerWorldGetter<ISkyEffect> getter) {
		INSTANCE.skyEffectGetter.register(getter);
	}
	
	/**
	 * registers per dimension resource handler. 
	 * @param handler the handler
	 * */
	public static void registerPerDimResourceHandler(IPerDimensionResourceHandler handler) {
		INSTANCE.resourceManager.register(handler);
	}
	
	
	public static EventBus getEventBus() {
		return INSTANCE.stellarEventBus;
	}
	
	
	/**
	 * Gets possible render types for certain dimension.
	 * @param worldName the name of the world; only provided information on the world
	 * */
	public static String[] getRenderTypesForDimension(String worldName) {
		List<String> strlist = Lists.newArrayList();
		for(ISkyRendererType type : INSTANCE.rendererTypes)
			if(type.acceptFor(worldName))
				strlist.add(type.getName());
		return strlist.toArray(new String[0]);
	}
	
	/**
	 * Gets renderer for certain option of sky renderer type.
	 * @param option the sky renderer type
	 * @param subRenderer renderer to be called for rendering celestial sphere
	 * */
	public static IRenderHandler getRendererFor(String option, ICelestialRenderer subRenderer) {
		for(ISkyRendererType type : INSTANCE.rendererTypes)
			if(type.getName().equals(option))
				return type.createSkyRenderer(subRenderer);
		return null;
	}
	
	/**
	 * Gets celestial coordinate for certain world.
	 * @param world the world
	 * @return the coordinate for the world if it exists, or <code>null</code> otherwise
	 * */
	public static ICelestialCoordinate getCoordinate(World world) {
		return INSTANCE.coordinateGetter.get(world, null);
	}
	
	/**
	 * Gets sky effect for certain world.
	 * @param world the world
	 * @return the sky effect for the world if it exists, or <code>null</code> otherwise
	 * */
	public static ISkyEffect getSkyEffect(World world) {
		return INSTANCE.skyEffectGetter.get(world, null);
	}
	
	/**
	 * Gets celestial light sources for certain world.
	 * @param world the world
	 * @return the light sources for the world if it exists, or <code>null</code> otherwise
	 * */
	public static CelestialLightSources getLightSources(World world) {
		return INSTANCE.lightSourcesGetter.get(world, null);
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
