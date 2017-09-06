package worldsets.api.provider;

@Deprecated
public interface IProviderRegistries {
	public <T extends IProvider> IProviderRegistry<T> findRegistryByType(Class<T> registryType);
}