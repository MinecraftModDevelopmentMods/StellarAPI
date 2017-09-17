package stellarapi.api.atmosphere;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IAtmSetProvider {
	/**
	 * Gets atmosphere ID for certain world.
	 * */
	public ResourceLocation atmosphereID(World world);

	/**
	 * Generates actual local atmosphere for certain world.
	 * This creates atmosphere from world regardless of the settings.
	 * */
	public ILocalAtmosphere generateLocalAtmosphere(World world);

	/**
	 * Gets the atmosphere type for certain world.
	 * */
	public AtmosphereType getAtmosphereType(World world);

	/**
	 * Gets the atmosphere settings from the type and existing settings.
	 * Only need to handle the types for this provider.
	 * */
	public JsonObject getAtmSettings(AtmosphereType type, Object atmSettings);

	/**
	 * Generates atmosphere settings.
	 * */
	public Object generateSettings();
}
