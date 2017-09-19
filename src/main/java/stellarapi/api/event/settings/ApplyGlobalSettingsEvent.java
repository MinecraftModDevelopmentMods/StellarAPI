package stellarapi.api.event.settings;

import net.minecraftforge.fml.common.eventhandler.GenericEvent;

/**
 * Apply global settings of specific class.
 * Always check for the identifier if it's right event.
 * */
public class ApplyGlobalSettingsEvent<S> extends GenericEvent<S> {
	public final Object settings;
	public final S identifier;

	public ApplyGlobalSettingsEvent(Class<S> baseClass, S ident, Object settings) {
		super(baseClass);
		this.identifier = ident;
		this.settings = settings;
	}
}