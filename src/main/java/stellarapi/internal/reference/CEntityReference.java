package stellarapi.internal.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.patch.BasePatchHandler;
import stellarapi.internal.coordinates.CLocalCoordinates;

public class CEntityReference implements ICapabilityProvider {

	private CLocalCoordinates coordinates = null;

	public CEntityReference(Entity entity) {
		if(BasePatchHandler.isLocationSpecific())
			this.coordinates = new CLocalCoordinates(entity, false);
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
