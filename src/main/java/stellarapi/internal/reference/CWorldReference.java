package stellarapi.internal.reference;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.coordinates.CCoordSystem;
import stellarapi.internal.coordinates.CLocalCoordinates;

public class CWorldReference implements ICapabilityProvider {

	private CCoordSystem system;
	private CLocalCoordinates coordinates;

	public CWorldReference(World world, CCoordSystem system) {
		this.coordinates = new CLocalCoordinates(world, true);
		this.system = system;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.LOCAL_COORDINATES && this.coordinates != null)
			return true;
		return capability == SAPICapabilities.COORDINATES_SYSTEM;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.LOCAL_COORDINATES)
			return SAPICapabilities.LOCAL_COORDINATES.cast(this.coordinates);
		else if(capability == SAPICapabilities.COORDINATES_SYSTEM)
			return SAPICapabilities.COORDINATES_SYSTEM.cast(this.system);
		else return null;
	}

}
