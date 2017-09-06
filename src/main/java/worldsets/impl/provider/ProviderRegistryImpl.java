package worldsets.impl.provider;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import worldsets.api.provider.IProviderRegistry;

public class ProviderRegistryImpl<T> implements IProviderRegistry<T> {

	private Class<T> providerType;
	private List<T> providers = Lists.newArrayList();
	private ImmutableList<T> providersAsImmutable;

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

}
