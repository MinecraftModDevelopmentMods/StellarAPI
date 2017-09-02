package stellarapi.internal.reference;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.internal.coordinates.CLocalCoordinates;

public class CWorldReference implements ICapabilityProvider {

	private CLocalCoordinates coordinates;

	public CWorldReference(World world) {
		this.coordinates = new CLocalCoordinates(world, true);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		return null;
	}

}
