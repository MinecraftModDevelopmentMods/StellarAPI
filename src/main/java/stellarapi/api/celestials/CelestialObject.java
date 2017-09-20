package stellarapi.api.celestials;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * Actual instance for certain celestial object.
 * */
public class CelestialObject implements ICapabilityProvider {

	private final CelestialType type;
	private final CapabilityDispatcher capabilities;

	protected CelestialObject(CelestialType type) {
		this.type = type;

		AttachCapabilitiesEvent<CelestialObject> event = new AttachCapabilitiesEvent(CelestialObject.class, this);
		MinecraftForge.EVENT_BUS.post(event);
		this.capabilities = event.getCapabilities().size() > 0? new CapabilityDispatcher(event.getCapabilities(), null) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.capabilities == null? false : capabilities.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.capabilities == null? null : capabilities.getCapability(capability, facing);
	}

}