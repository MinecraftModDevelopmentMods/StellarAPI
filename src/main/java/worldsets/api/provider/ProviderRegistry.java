package worldsets.api.provider;

public class ProviderRegistry {

	private static IProviderRegistries registry;

	public static <T extends IProvider> IProviderRegistry<T> findRegistry(Class<T> registryType) {
		return registry.findRegistryByType(registryType);
	}

}
