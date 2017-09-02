package stellarapi.internal.reference;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.patch.BasePatchHandler;
import stellarapi.internal.coordinates.CLocalCoordinates;

public class CTileEntityReference implements ICapabilityProvider {

	private CLocalCoordinates coordinates;

	public CTileEntityReference(TileEntity tileentity) {
		if(BasePatchHandler.isLocationSpecific())
			this.coordinates = new CLocalCoordinates(tileentity, false);
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
