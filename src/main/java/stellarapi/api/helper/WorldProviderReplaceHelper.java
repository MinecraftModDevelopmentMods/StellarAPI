package stellarapi.api.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class WorldProviderReplaceHelper {

	private static final Field providerField = ReflectionHelper.findField(World.class,
			ObfuscationReflectionHelper.remapFieldNames(World.class.getName(), "provider", "field_73011_w"));

	static {
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(providerField, providerField.getModifiers() & ~Modifier.FINAL);
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	/**
	 * Patches {@link WorldProvider} with given provider.
	 * 
	 * @param world
	 *            the world
	 * @param provider
	 *            the world provider to replace original provider
	 */
	public static void patchWorldProviderWith(World world, WorldProvider provider) {
		try {
			providerField.set(world, provider);
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

}
