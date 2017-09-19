package stellarapi.api;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SAPIUtility {
	/**
	 * Registers the object when there's no duplication.
	 * Returns true if the object is registered.
	 * */
	public static <V extends IForgeRegistryEntry<V>> boolean registerSafe(
			IForgeRegistry<V> registry, V object) {
		if(registry.containsKey(object.getRegistryName()))
			return false;

		registry.register(object);
		return true;
	}
}
