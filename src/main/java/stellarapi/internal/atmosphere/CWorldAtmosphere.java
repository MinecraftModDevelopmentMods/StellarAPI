package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmSetProvider;
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
	private boolean atmosphereSetup = false;

	public CWorldAtmosphere(World world, WorldSet worldSet) {
		this.world = world;
		this.worldSet = worldSet;
	}

	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; this.atmosphereSetup = true; }
	@Override
	public boolean isAtmosphereSetup() { return this.atmosphereSetup; }

	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return this.local; }

	@Override
	public void reevaluateAtmosphere(Object atmSettings) {
		WorldSetInstance instance = WAPIReference.getWorldSetInstance(this.world, this.worldSet);
		IAtmSystem system = instance.getCapability(SAPICapabilities.ATMOSPHERE_SYSTEM, null);
		ResourceLocation newProviderID = system.getProviderID();
		if(this.currentProviderID != newProviderID && newProviderID != null) {
			IAtmSetProvider provider = system.getSetProvider();
			this.local = provider.generateLocalAtmosphere(this.world);
			this.atmosphere = atmSettings == null?
					provider.genBlankAtmosphere(this.world)
					: provider.generateAtmosphere(this.world, atmSettings);

			this.currentProviderID = newProviderID;
			this.atmosphereSetup = true;
		} else if(newProviderID != null) {
			IAtmSetProvider provider = system.getSetProvider();
			if(provider.replaceWithSettings(this.world, atmSettings))
				this.atmosphere = provider.generateAtmosphere(this.world, atmSettings);
		}
	}
}