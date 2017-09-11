package stellarapi.api.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface IAtmosphereType {
	/**
	 * Gets atmosphere ID.
	 * */
	public ResourceLocation atmosphereID(World world);
}