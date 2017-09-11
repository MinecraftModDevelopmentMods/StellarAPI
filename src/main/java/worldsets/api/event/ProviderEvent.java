package worldsets.api.event;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import worldsets.api.provider.IProvider;
import worldsets.api.provider.IProviderRegistry;

/**
 * Fired to register providers.
 * */
public class ProviderEvent<P extends IProvider> extends GenericEvent<P> {

	protected ProviderEvent(Class<P> type) {
		super(type);
	}

	/**
	 * Build registry within this event.
	 * */
	public static class NewRegistry extends Event { }

	/**
	 * Register providers with this event.
	 * */
	public static class Register<P extends IProvider> extends ProviderEvent<P> {
		public final IProviderRegistry<P> registry;

		public Register(IProviderRegistry<P> registry) {
			super(registry.getProviderType());
			this.registry = registry;
		}
	}

	/**
	 * Called on WorldEvent.Load for default world to apply global settings for each provider.
	 * Partial initiation for cross reference on provider-specific objects
	 *  should be done here.
	 * */
	public static class ApplySettings<P extends IProvider> extends ProviderEvent<P> {
		public final IProviderRegistry<P> registry;

		public ApplySettings(IProviderRegistry<P> registry) {
			super(registry.getProviderType());
			this.registry = registry;
		}
	}

	/**
	 * Gathers the information to NBT for the sync packet.
	 * Always called on server side.
	 * */
	public static class Send<P extends IProvider> extends ProviderEvent<P> {
		public final IProviderRegistry<P> registry;
		/** The world to send data for */
		public final World world;
		public final NBTTagCompound compoundToSend;

		public Send(IProviderRegistry<P> registry, World world, NBTTagCompound compToSend) {
			super(registry.getProviderType());
			this.registry = registry;
			this.world = world;
			this.compoundToSend = compToSend;
		}
	}

	/**
	 * Provide NBT from the sync packet to synchronize with the server.
	 * Always called on client side.
	 * Partial initiation for cross reference on provider-specific objects
	 *  should be done here.
	 * */
	public static class Receive<P extends IProvider> extends ProviderEvent<P> {
		public final IProviderRegistry<P> registry;
		/** The world to receive this packet for */
		public final World world;
		public final NBTTagCompound receivedCompound;

		public Receive(IProviderRegistry<P> registry, World world, NBTTagCompound received) {
			super(registry.getProviderType());
			this.registry = registry;
			this.world = world;
			this.receivedCompound = received;
		}
	}

	/**
	 * Called to complete provider-specific global objects for further reference.
	 * It's invoked twice on client, for pre-sync placeholder and synced object.
	 * */
	public static class Complete<P extends IProvider> extends ProviderEvent<P> {
		public final IProviderRegistry<P> registry;
		/**
		 * True for placeholder when server-side provider does not exist.
		 * */
		public final boolean forPlaceholder;

		public Complete(IProviderRegistry<P> registry, boolean placeholder) {
			super(registry.getProviderType());
			this.registry = registry;
			this.forPlaceholder = placeholder;
		}
	} 
}