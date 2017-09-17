package stellarapi.api.atmosphere;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class AtmosphereType extends IForgeRegistryEntry.Impl<AtmosphereType> {
	/**
	 * Creates generic atmosphere with the json settings.
	 * If the settings is empty, this will generate the atmosphere as empty.
	 * It will be filled with the saved data.
	 * */
	public abstract Atmosphere generateAtmosphere(@Nullable JsonObject settings);
}