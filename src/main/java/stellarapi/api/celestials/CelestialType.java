package stellarapi.api.celestials;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

/**
 * Certain celestial type.
 * */
public class CelestialType extends Impl<CelestialType> {

	//Category of this type.
	private EnumCelestialCategory category = EnumCelestialCategory.UNKNOWN;

	/**
	 * Sets category.
	 * */
	void setCategory(EnumCelestialCategory category) { this.category = category; }

}