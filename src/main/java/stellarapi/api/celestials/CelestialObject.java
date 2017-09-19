package stellarapi.api.celestials;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * Actual instance for certain celestial object.
 * */
public class CelestialObject implements ICapabilityProvider {

	private final CelestialType type;
	private final CapabilityDispatcher capabilities = null;

	protected CelestialObject(CelestialType type) {
		this.type = type;
		// TODO dispatch capabilities
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.capabilities == null? false : capabilities.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.capabilities == null? null : capabilities.getCapability(capability, facing);
	}

}