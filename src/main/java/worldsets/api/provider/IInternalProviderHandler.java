package worldsets.api.provider;

import java.util.Comparator;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.ResourceLocation;
import worldsets.api.provider.IProviderRegistry.AddCallback;
import worldsets.api.provider.IProviderRegistry.ClearCallback;
import worldsets.api.provider.IProviderRegistry.CreateCallback;
import worldsets.api.provider.IProviderRegistry.SubstitutionCallback;

@Deprecated
public interface IInternalProviderHandler {
	@Deprecated
	public <T extends IProvider> IProviderRegistry<T> findRegistryByType(Class<T> registryType);
	@Deprecated
	public ImmutableMap<ResourceLocation, IProviderRegistry<?>> getProviderRegistryMap();
	@Deprecated
	public <T extends IProvider> IProviderRegistry<T> createRegistry(ResourceLocation registryName, Class<T> registryType, boolean addToFirst,
			@Nullable Comparator<T> comparator, @Nullable AddCallback<T> add, @Nullable CreateCallback<T> create,
			@Nullable ClearCallback<T> clear, @Nullable SubstitutionCallback<T> substitution);
}