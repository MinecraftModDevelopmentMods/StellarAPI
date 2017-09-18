package stellarapi.internal.atmosphere;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.atmosphere.IAtmProvider;
import stellarapi.api.atmosphere.IAtmSetProvider;
import stellarapi.api.atmosphere.IAtmSystem;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.ProviderRegistry;
import worldsets.api.worldset.WorldSet;

public class CAtmSystem implements IAtmSystem {

	private WorldSet worldSet;
	private IProviderRegistry<IAtmProvider> registry;
	private ResourceLocation currentProviderID;
	private IAtmSetProvider setProvider;

	public CAtmSystem(WorldSet worldSet) {
		this.worldSet = worldSet;
		this.registry = ProviderRegistry.findRegistry(IAtmProvider.class);
	}

	@Override
	public void setProviderID(ResourceLocation setProviderID) {
		this.currentProviderID = setProviderID;
		this.setProvider = registry.getProvider(this.currentProviderID).perSetProvider(worldSet);
	}

	@Override
	public ResourceLocation getProviderID() {
		return this.currentProviderID;
	}

	@Override
	public IAtmSetProvider getSetProvider() {
		return this.setProvider;
	}

}
