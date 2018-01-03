package stellarapi.api.gui.overlay;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.Loader;
import worldsets.api.lib.config.ConfigManager;

@SuppressWarnings("rawtypes")
public class OverlayRegistry {

	private static OverlayRegistry INSTANCE = new OverlayRegistry();

	private Map<String, IOverlaySetType> mapSet = Maps.newHashMap();
	private Map<String, RegistryDelegate> mapOverlay = Maps.newHashMap();

	/**
	 * Registers certain overlay set. One can override pre-existing overlay with
	 * using same id.
	 * 
	 * @param id
	 *            the id of the overlay set
	 * @param overlaySet
	 *            the type of the overlay set
	 */
	public static void registerOverlaySet(String id, IOverlaySetType overlaySet) {
		INSTANCE.mapSet.put(id, overlaySet);
	}

	/**
	 * Registers certain overlay. One can override pre-existing overlay with
	 * using same id.
	 * 
	 * @param id
	 *            the id of the overlay
	 * @param type
	 *            the type of the overlay
	 * @param config
	 *            the configuration instance to save the settings
	 */
	public static <E extends IOverlayElement<S>, S extends PerOverlaySettings> void registerOverlay(String id,
			IOverlayType<E, S> type, ConfigManager config) {
		String modid = Loader.instance().activeModContainer().getModId();
		INSTANCE.mapOverlay.put(id, INSTANCE.new RegistryDelegate<E,S>(type, config, modid));
	}

	/**
	 * Sets up overlay. Used for internal logic.
	 * 
	 * @param injectable
	 *            the overlay injectable
	 */
	public static void setupOverlay(IOverlayInjectable injectable) {
		for (IOverlaySetType type : INSTANCE.mapSet.values())
			injectable.injectOverlaySet(type);
		for (Map.Entry<String, RegistryDelegate> entry : INSTANCE.mapOverlay.entrySet()) {
			entry.getValue().inject(entry.getKey(), injectable);
		}
	}

	private class RegistryDelegate<E extends IOverlayElement<S>, S extends PerOverlaySettings> {

		private final IOverlayType<E, S> type;
		private final S settings;
		private final ConfigManager config;
		private final String modid;

		private RegistryDelegate(IOverlayType<E, S> type, ConfigManager config, String modid) {
			this.type = type;
			this.settings = type.generateSettings();
			this.config = config;
			this.modid = modid;

			config.register(type.getName(), this.settings);
			settings.initializeSetttings(type.defaultHorizontalPos(), type.defaultVerticalPos(), type.isOnMain());
		}

		public void inject(String id, IOverlayInjectable injectable) {
			injectable.injectOverlay(id, modid, type, settings, config);
		}
	}

}
