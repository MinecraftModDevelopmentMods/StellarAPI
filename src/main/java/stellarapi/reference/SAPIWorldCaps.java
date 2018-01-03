package stellarapi.reference;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;

public class SAPIWorldCaps implements ICapabilityProvider {

	private CelestialPackManager celestials;

	public SAPIWorldCaps(World world) {
		this.celestials = new CelestialPackManager(world);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.CELESTIAL_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.CELESTIAL_CAPABILITY)
			return SAPICapabilities.CELESTIAL_CAPABILITY.cast(this.celestials);
		else return null;
	}

}
