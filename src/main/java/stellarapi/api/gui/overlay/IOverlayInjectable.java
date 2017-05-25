package stellarapi.api.gui.overlay;

import stellarapi.api.lib.config.DynamicConfigManager;

/**
 * Used for internal logic related with registration.
 */
public interface IOverlayInjectable {
	public void injectOverlaySet(IOverlaySetType type);

	public <E extends IOverlayElement<S>, S extends PerOverlaySettings> void injectOverlay(String id, String modid,
			IOverlayType<E, S> type, S settings, DynamicConfigManager notified);
}
