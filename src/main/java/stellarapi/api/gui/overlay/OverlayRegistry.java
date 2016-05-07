package stellarapi.api.gui.overlay;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarapi.api.lib.config.ConfigManager;

public class OverlayRegistry {
	
	static OverlayRegistry INSTANCE = new OverlayRegistry();
	
	private Map<String, RegistrySetDelegate> map = Maps.newHashMap();
	
	/**
	 * Registers certain overlay set.
	 * @param id the id of the overlay set
	 * @param langKet the language key for the overlay set
	 * */
	public static void registerOverlaySet(String id, String langKey) {
		INSTANCE.map.put(id, INSTANCE.new RegistrySetDelegate(langKey));
	}
	
	/**
	 * Registers certain overlay.
	 * @param idSet the id of the overlay set of which the overlay is registered
	 * @param type the type of the overlay
	 * @param config the config instance to save the settings
	 * */
	public static <E extends IGuiOverlay<S>, S extends PerOverlaySettings> void registerOverlay(
			String idSet, IGuiOverlayType<E, S> type, ConfigManager config) {
		if(!INSTANCE.map.containsKey(idSet))
			throw new IllegalArgumentException(String.format("Overlay set %s is not yet registered!", idSet));
		
		INSTANCE.map.get(idSet).register(INSTANCE.new RegistryDelegate(type, config));
	}
	
	private class RegistrySetDelegate {
		
		private String langKey;
		private List<RegistryDelegate> delegates = Lists.newArrayList();
		
		public RegistrySetDelegate(String langKey) {
			this.langKey = langKey;
		}

		public void register(RegistryDelegate registryDelegate) {
			delegates.add(registryDelegate);
		}
	}
	
	private class RegistryDelegate<E extends IGuiOverlay<S>, S extends PerOverlaySettings> {
		
		private final IGuiOverlayType<E, S> type;
		private final S settings;
		private final ConfigManager config;
		
		private RegistryDelegate(IGuiOverlayType<E, S> type, ConfigManager config) {
			this.type = type;
			this.settings = type.generateSettings();
			this.config = config;
			
			config.register(type.getName(), this.settings);
			settings.initializeSetttings(type.defaultHorizontalPos(), type.defaultVerticalPos());
		}
	}

}
