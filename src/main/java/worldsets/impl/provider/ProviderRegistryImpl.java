package worldsets.impl.provider;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;

public class ProviderRegistryImpl<T extends IProvider> implements IProviderRegistry<T> {

	private Class<T> providerType;
	private List<T> providers = Lists.newArrayList();
	private Map<ResourceLocation, T> providersAsImmutable;

	// TODO Providers fill in details

	@Override
	public void register(T provider) {
		providers.add(provider);
	}

	@Override
	public Iterable<T> providers() {
		return this.providers;
	}

	@Override
	public Class<T> getProviderType() {
		return this.providerType;
	}

	@Override
	public T getProvider(ResourceLocation registryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<T> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
