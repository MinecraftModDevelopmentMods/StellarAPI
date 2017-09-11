package worldsets.api.provider;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/**
 * Registry for providers.
 * remove operation of the main iterable iterator won't work here.
 * */
public interface IProviderRegistry<P extends IProvider> extends Iterable<P> {

	/**
	 * Registers certain provider.
	 * */
	public void register(@Nonnull ResourceLocation key, @Nonnull P provider);

	/**
	 * Whether this registry contains certain key or not.
	 * */
	public boolean containsKey(ResourceLocation key);

	/**
	 * Gets provider for certain key.
	 * Gives null if there's neither mapping for the specified key nor the default provider.
	 * */
	public @Nullable P getProvider(@Nonnull ResourceLocation key);

	/**
	 * Gets the keys in order. Remove operation is not supported here.
	 * */
	public Iterable<ResourceLocation> keys();

	/**
	 * Gets the default key. the value mapped on this key will be the default value,
	 * which is returned on any keys without mapping.
	 * 
	 * This could be null when there's no default key.
	 * */
	public @Nullable ResourceLocation getDefaultKey();

	/**
	 * Substitutes the provider on the key.
	 * Before substitution, ensure that the original provider is registered.
	 * */
	public void substitute(@Nonnull ResourceLocation key,@Nonnull P replacement);

	/**
	 * Retrieve the slave map of type T from the registry.
	 * Slave maps are maps which are dependent on registry content in some way.
	 * @param slaveMapName The name of the slavemap
	 * @param type The type
	 * @param <T> Type to return
	 * @return The slavemap if present
	 */
	public <T> T getSlaveMap(@Nonnull ResourceLocation slaveMapName, @Nonnull Class<T> type);

	/**
	 * Gets the provider type.
	 * */
	public Class<P> getProviderType();


	/**
	 * Callback fired when objects are added to the registry. This will fire when the registry is rebuilt
	 * on the client side from a server side synchronization, or when a world is loaded.
	 */
	interface AddCallback<P extends IProvider> {
		public void onAdd(ResourceLocation key, P provider, Map<ResourceLocation, ?> slaveset);
	}

	/**
	 * Callback fired when a registry instance is created. Populate slave maps here.
	 */
	interface CreateCallback<P extends IProvider> {
		public void onCreate(Map<ResourceLocation, ?> slaveset);
	}

	/**
	 * Callback fired when clearing the registry on client world load.
	 * */
	interface ClearCallback<P extends IProvider> {
		public void onClear(IProviderRegistry<P> registry, Map<ResourceLocation, ?> slaveset);
	}

	/**
	 * Callback fired when a provider is substituted.
	 * */
	interface SubstitutionCallback<P extends IProvider> {
		public void onSubstitution(Map<ResourceLocation, ?> slaveset, ResourceLocation key, P original, P replacement);
	}
}