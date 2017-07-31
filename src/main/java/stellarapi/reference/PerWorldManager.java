package stellarapi.reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.ICelestialCoordinate;
import stellarapi.api.ISkyEffect;
import stellarapi.api.StellarAPICapabilities;
import stellarapi.api.StellarAPIReference;
import stellarapi.api.event.SetCoordinateEvent;
import stellarapi.api.event.SetSkyEffectEvent;

/**
 * Per world manager to contain the per-world(dimension) objects.
 * TODO appropriate revamp needed
 */
public class PerWorldManager implements ICapabilityProvider {

	private World world;

	private CelestialSceneImpl celestials = null;
	private ICelestialCoordinate coordinate = null;
	private ISkyEffect skyEffect = null;

	public PerWorldManager(World world) {
		this.world = world;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == StellarAPICapabilities.CELESTIAL_CAPABILITY) {
			return true;
		} else if(capability == StellarAPICapabilities.COORDINATE_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == StellarAPICapabilities.CELESTIAL_CAPABILITY) {
			
		} else if(capability == StellarAPICapabilities.COORDINATE_CAPABILITY) {
			
		}
		return null;
	}

	public void setCoordinate() {
		if(this.coordinate != null)
			return;

		SetCoordinateEvent coord = new SetCoordinateEvent(this.world);
		if (StellarAPIReference.getEventBus().post(coord))
			return;
		this.coordinate = coord.getCoordinate();
	}

	public void setSkyEffect() {
		if(this.skyEffect != null)
			return;

		SetSkyEffectEvent sky = new SetSkyEffectEvent(this.world);
		if (StellarAPIReference.getEventBus().post(sky))
			return;
		this.skyEffect = sky.getSkyEffect();
	}

	public ICelestialCoordinate getCoordinate() {
		return this.coordinate;
	}

	public ISkyEffect getSkyEffect() {
		return this.skyEffect;
	}
}
