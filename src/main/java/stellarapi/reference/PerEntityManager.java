package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;

public class PerEntityManager implements ICapabilityProvider {

	private OpticalViewerEventCallback scope;

	public PerEntityManager(Entity entity) {
		this.scope = new OpticalViewerEventCallback(entity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == SAPICapabilities.VIEWER_CAPABILITY)
			return true;
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == SAPICapabilities.VIEWER_CAPABILITY)
			return SAPICapabilities.VIEWER_CAPABILITY.cast(this.scope);
		return null;
	}

}
