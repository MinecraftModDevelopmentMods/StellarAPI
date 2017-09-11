package stellarapi.api.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class AtmosphereType {
	/**
	 * Gets atmosphere ID.
	 * */
	public abstract ResourceLocation atmosphereID(World world);
}