package stellarapi.internal.reference;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.celestial.CelestialScene;
import stellarapi.internal.coordinates.CLocalCoordinates;

public class CWorldReference implements ICapabilityProvider {

	private final CLocalCoordinates coordinates;
	private final CelestialScene scene;

	public CWorldReference(World world) {
		this.coordinates = new CLocalCoordinates(world, true);
		this.scene = new CelestialScene();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.LOCAL_COORDINATES;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.LOCAL_COORDINATES)
			return SAPICapabilities.LOCAL_COORDINATES.cast(this.coordinates);
		else return null;
	}
}
