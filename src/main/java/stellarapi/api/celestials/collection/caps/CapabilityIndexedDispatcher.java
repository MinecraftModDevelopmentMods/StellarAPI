package stellarapi.api.celestials.collection.caps;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/** 
 * Attached on the creation of TypedModel.
 * */
public class CapabilityIndexedDispatcher implements ICapabilityProvider {

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Collection indexedDispatcher
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// TODO Collection indexedDispatcher
		return null;
	}

}
