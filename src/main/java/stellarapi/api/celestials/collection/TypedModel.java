package stellarapi.api.celestials.collection;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.celestials.collection.caps.CapabilityIndexedDispatcher;

public class TypedModel implements ICapabilityProvider {

	// TODO custom dispatcher & event
	private CapabilityIndexedDispatcher capabilities = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.hasCapability(capability, facing) : false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.capabilities != null? capabilities.getCapability(capability, facing) : null;
	}

}
