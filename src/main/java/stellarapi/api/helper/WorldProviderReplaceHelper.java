package stellarapi.api.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class WorldProviderReplaceHelper {
	
	private static final Field providerField = ReflectionHelper.findField(World.class,
			ObfuscationReflectionHelper.remapFieldNames(World.class.getName(), "provider", "field_73011_w"));
	
	static {
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(providerField, providerField.getModifiers() & ~ Modifier.FINAL);
		} catch(Exception exc) {
			Throwables.propagate(exc);
		}
	}
	
	/**
	 * Patches {@link WorldProvider} with given provider.
	 * @param world the world
	 * @param provider the world provider to replace original provider
	 * */
	public void patchWorldProviderWith(World world, WorldProvider provider) {
		try {
			providerField.set(world, provider);
		} catch (Exception exc) {
			Throwables.propagate(exc);
		}
	}

}
