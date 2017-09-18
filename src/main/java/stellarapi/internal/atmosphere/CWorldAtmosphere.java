package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmSystem;
import stellarapi.api.atmosphere.ILocalAtmosphere;
import worldsets.api.WAPIReference;
import worldsets.api.worldset.WorldSet;
import worldsets.api.worldset.WorldSetInstance;

/**
 * Atmosphere for world, supports local atmosphere.
 * */
public class CWorldAtmosphere implements IAtmHolder {

	private World world;
	private WorldSet worldSet;

	private Atmosphere atmosphere = null;
	private ILocalAtmosphere local = null;
	private ResourceLocation currentProviderID = null;

	public CWorldAtmosphere(World world, WorldSet worldSet) {
		this.world = world;
		this.worldSet = worldSet;
	}

	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; }

	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return this.local; }
	@Override
	public void reevaluateLocalAtmosphere() {
		WorldSetInstance instance = WAPIReference.getWorldSetInstance(this.world, this.worldSet);
		IAtmSystem system = instance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
		ResourceLocation newProviderID = system.getProviderID();
		if(this.currentProviderID != newProviderID && newProviderID != null) {
			this.local = system.getSetProvider().generateLocalAtmosphere(this.world);
			this.currentProviderID = newProviderID;
		}
	}

}
