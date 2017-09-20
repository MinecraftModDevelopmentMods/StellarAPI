package stellarapi.internal.reference;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.internal.atmosphere.CWorldAtmosphere;
import stellarapi.internal.celestial.CelestialScene;
import stellarapi.internal.celestial.CelestialSystem;
import stellarapi.internal.coordinates.CCoordSystem;
import stellarapi.internal.coordinates.CLocalCoordinates;
import worldsets.api.worldset.WorldSet;

public class CWorldReference implements ICapabilityProvider {

	// Locals
	private final CLocalCoordinates coordinates;
	private final CelestialScene scene;
	private final CWorldAtmosphere atmosphere;

	// Systems
	private final CelestialSystem celSystem;
	private final CCoordSystem coordSystem;

	public CWorldReference(World world, WorldSet worldSet) {
		this.coordinates = new CLocalCoordinates(world, true);
		this.scene = new CelestialScene();
		this.atmosphere = new CWorldAtmosphere(world, worldSet);

		this.celSystem = new CelestialSystem(worldSet);
		this.coordSystem = new CCoordSystem(worldSet);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Set up these things
		return capability == SAPICapabilities.LOCAL_COORDINATES;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == SAPICapabilities.LOCAL_COORDINATES)
			return SAPICapabilities.LOCAL_COORDINATES.cast(this.coordinates);
		else return null;
	}
}
