package stellarapi.api.gui.overlay;

import worldsets.api.lib.config.ConfigManager;

/**
 * Used for internal logic related with registration.
 */
public interface IOverlayInjectable {
	public void injectOverlaySet(IOverlaySetType type);

	public <E extends IOverlayElement<S>, S extends PerOverlaySettings> void injectOverlay(String id, String modid,
			IOverlayType<E, S> type, S settings, ConfigManager notified);
}
