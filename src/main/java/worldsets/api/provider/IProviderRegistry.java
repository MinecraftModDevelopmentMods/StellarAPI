package worldsets.api.provider;

import net.minecraft.util.ResourceLocation;

public interface IProviderRegistry<T extends IProvider> {

	/**
	 * Registers certain provider.
	 * */
	public void register(T provider);

	/**
	 * Gets provider for certain registry name.
	 * */
	public T getProvider(ResourceLocation registryName);

	/**
	 * Iterable for the keys in order.
	 * */
	public Iterable<T> getKeys();

	/**
	 * Iterable for the providers in order.
	 * Does not available in registry phase.
	 * One can't remove any elements with this provider.
	 * */
	public Iterable<T> providers();


	/**
	 * Gets the provider type.
	 * */
	public Class<T> getProviderType();
}