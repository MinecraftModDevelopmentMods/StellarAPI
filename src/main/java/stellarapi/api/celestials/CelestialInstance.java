package stellarapi.api.celestials;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.CelestialPeriod;
import stellarapi.api.lib.math.Vector3;

/**
 * Actual instance for certain celestial object.
 * */
public class CelestialInstance implements ICapabilityProvider {

	private final CelestialType type;

	//Current Absolute position.
	private Vector3 currentAbsPos;

	protected CelestialInstance(CelestialType type) {
		this.type = type;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO How to gather the capability?
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO How to gather the capability?
		return null;
	}

}