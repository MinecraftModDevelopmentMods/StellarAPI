package worldsets.impl.provider;

import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import scala.reflect.internal.Trees.This;
import worldsets.api.provider.IInternalProviderHandler;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;
import worldsets.api.provider.IProviderRegistry.AddCallback;
import worldsets.api.provider.IProviderRegistry.ClearCallback;
import worldsets.api.provider.IProviderRegistry.CreateCallback;
import worldsets.api.provider.IProviderRegistry.SubstitutionCallback;

public enum ProviderRegistries implements IInternalProviderHandler {
	ACTIVE, BASIS, STAGING;

	private final Map<ResourceLocation, ProviderRegistryImpl<?>> registryMap = Maps.newHashMap();
    private final BiMap<Class<? extends IProvider>, ResourceLocation> registrySuperTypes = HashBiMap.create();

	@Override
	public <T extends IProvider> IProviderRegistry<T> findRegistryByType(Class<T> registryType) {
		return this.getRegistry(registrySuperTypes.get(registryType), registryType);
	}

	private <T extends IProvider> ProviderRegistryImpl<T> getRegistry(ResourceLocation id, Class<T> regType) {
		return (ProviderRegistryImpl<T>) registryMap.get(id);
	}

    private <T extends IProvider> ProviderRegistryImpl<T> getOrShallowCopyRegistry(ResourceLocation key, Class<T> regType, ProviderRegistryImpl<T> other)
    {
        if (!registryMap.containsKey(key))
        {
        	registryMap.put(key, other.makeShallowCopy(registryMap));
            registrySuperTypes.put(regType, key);
        }
        return getRegistry(key, regType);
    }

	@Override
	public ImmutableMap<ResourceLocation, IProviderRegistry<?>> getProviderRegistryMap() {
		// TODO Use Dependency for registries
		return null;
	}

	@Override
	public <T extends IProvider> IProviderRegistry<T> createRegistry(ResourceLocation registryName,
			Class<T> registryType, boolean addToFirst, Comparator<T> comparator, AddCallback<T> add,
			CreateCallback<T> create, ClearCallback<T> clear, SubstitutionCallback<T> substitution) {
		// TODO Create registry here
		return null;
	}

	public void clear() {
		registryMap.clear();
		registrySuperTypes.clear();
	}

	public static void syncRegistry() {
		// TODO fill in these; how to go?
		ProviderRegistries.STAGING.clear();
	}

	public static void unsyncRegistry() {
		ProviderRegistries.STAGING.clear();
	}

}