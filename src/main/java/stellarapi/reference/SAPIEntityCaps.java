package stellarapi.reference;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;

public class SAPIEntityCaps implements ICapabilityProvider {

	private OpticalViewerEventCallback scope;

	public SAPIEntityCaps(Entity entity) {
		this.scope = new OpticalViewerEventCallback(entity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SAPICapabilities.VIEWER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == SAPICapabilities.VIEWER_CAPABILITY)
			return SAPICapabilities.VIEWER_CAPABILITY.cast(this.scope);
		else return null;
	}

}
