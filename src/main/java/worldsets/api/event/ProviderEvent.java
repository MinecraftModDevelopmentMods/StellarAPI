package worldsets.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;

/**
 * Fired to register providers.
 * */
public class ProviderEvent<T extends IProvider> extends GenericEvent<T> {

	protected ProviderEvent(Class<T> type) {
		super(type);
	}

	public static class NewRegistry extends Event { }

	public static class Register<T extends IProvider> extends ProviderEvent<T> {
		public final IProviderRegistry<T> registry;

		public Register(IProviderRegistry<T> registry) {
			super(registry.getProviderType());
			this.registry = registry;
		}
	}

}