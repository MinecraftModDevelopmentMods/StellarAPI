package worldsets.impl.provider;

import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistries;
import worldsets.api.provider.IProviderRegistry;

public class ProviderRegistries implements IProviderRegistries {

	@Override
	public <T extends IProvider> IProviderRegistry<T> findRegistryByType(Class<T> registryType) {
		// TODO Auto-generated method stub
		return null;
	}

}
