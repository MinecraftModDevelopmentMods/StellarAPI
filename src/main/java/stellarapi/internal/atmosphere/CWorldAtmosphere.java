package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.atmosphere.Atmosphere;
import stellarapi.api.atmosphere.IAtmHolder;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.atmosphere.ILocalAtmosphere;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

/**
 * Atmosphere for world, supports local atmosphere.
 * */
public class CWorldAtmosphere implements IAtmHolder {

	private World world;
	private WorldSet worldSet;

	private ResourceLocation prevID = null;
	private ResourceLocation providerID = null;
	private IAtmSetProvider atmProvider = null;

	private Atmosphere atmosphere = null;
	private ILocalAtmosphere local = null;
	private boolean atmosphereSetup = false;

	private IProviderRegistry<IAtmProvider> provReg = ProviderRegistry.findRegistry(IAtmProvider.class);

	public CWorldAtmosphere(World world, WorldSet worldSet) {
		this.world = world;
		this.worldSet = worldSet;
	}

	@Override
	public void setProviderID(ResourceLocation providerID) {
		if(!provReg.containsKey(providerID))
			throw new IllegalArgumentException(String.format("There's no provider for providerID %s", providerID));
		this.prevID = this.providerID;
		this.providerID = providerID;
		this.atmProvider = provReg.getProvider(providerID).perSetProvider(worldSet);
	}

	@Override
	public ResourceLocation getProviderID() { return this.providerID; }

	@Override
	public IAtmSetProvider getSetProvider() { return this.atmProvider; }


	@Override
	public Atmosphere getAtmosphere() { return this.atmosphere; }
	@Override
	public void setAtmosphere(Atmosphere atm) { this.atmosphere = atm; }


	@Override
	public ILocalAtmosphere getLocalAtmosphere() { return this.local; }

	@Override
	public void evaluateAtmosphere(Object atmSettings) {
		if(this.prevID != this.providerID) {
			this.local = atmProvider.generateLocalAtmosphere(this.world);
			this.atmosphere = atmSettings == null?
					atmProvider.genBlankAtmosphere(this.world)
					: atmProvider.generateAtmosphere(this.world, atmSettings);

			this.prevID = this.providerID;
		} else {
			if(atmProvider.replaceWithSettings(this.world, atmSettings))
				this.atmosphere = atmProvider.generateAtmosphere(this.world, atmSettings);
		}
	}
}