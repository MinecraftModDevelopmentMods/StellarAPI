package stellarapi.api.event.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import stellarapi.api.event.PerWorldEvent;

/**
 * World events on server.
 * */
public class ServerWorldEvent extends PerWorldEvent {
	
	private MinecraftServer server;
	
	public ServerWorldEvent(MinecraftServer server, World world) {
		super(world);
		this.server = server;
	}
	
	public MinecraftServer getServer() {
		return this.server;
	}
	
	/**
	 * Initial world load event, which will only be called for the Overworld.
	 * Overworld will get this.
	 * */
	public static class Initial extends ServerWorldEvent {
		public Initial(MinecraftServer server, World world) {
			super(server, world);
		}
	}
	
	/**
	 * Load event.
	 * */
	public static class Load extends ServerWorldEvent {
		public Load(MinecraftServer server, World world) {
			super(server, world);
		}
	}
	
	/**
	 * Unload event.
	 * */
	public static class Unload extends ServerWorldEvent {
		public Unload(MinecraftServer server, World world) {
			super(server, world);
		}
	}

}
