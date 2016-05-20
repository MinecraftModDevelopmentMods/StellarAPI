package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.StellarAPICapabilities;

public class PerEntityManager implements ICapabilityProvider {
	
	private Entity entity;
	private OpticalViewerEventCallback scope;

	public PerEntityManager(Entity entity) {
		this.entity = entity;
		this.scope = new OpticalViewerEventCallback(entity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == StellarAPICapabilities.VIEWER_CAPABILITY)
			return true;
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == StellarAPICapabilities.VIEWER_CAPABILITY)
			return (T) this.scope;
		return null;
	}

}
