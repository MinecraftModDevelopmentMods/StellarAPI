package stellarapi.api;

import java.util.List;

import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

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
