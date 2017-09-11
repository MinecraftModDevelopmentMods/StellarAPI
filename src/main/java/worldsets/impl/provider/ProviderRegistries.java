package worldsets.impl.provider;

import java.util.Comparator;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;
import worldsets.api.provider.IInternalProviderHandler;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.IProviderRegistry.*;

public class ProviderRegistries implements IInternalProviderHandler {

	@Override
	public <T extends IProvider> IProviderRegistry<T> findRegistryByType(Class<T> registryType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableMap<ResourceLocation, IProviderRegistry<?>> getProviderRegistryMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IProvider> IProviderRegistry<T> createRegistry(ResourceLocation registryName,
			Class<T> registryType, boolean addToFirst, Comparator<T> comparator, AddCallback<T> add,
			CreateCallback<T> create, ClearCallback<T> clear, SubstitutionCallback<T> substitution) {
		// TODO Auto-generated method stub
		return null;
	}

}
