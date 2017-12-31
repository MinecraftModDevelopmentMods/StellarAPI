package stellarapi.api.event.world;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.event.PerWorldEvent;

/**
 * World events on client.
 */
public class ClientWorldEvent extends PerWorldEvent {

	public ClientWorldEvent(World world) {
		super(world);
	}

	/**
	 * Load event.
	 */
	public static class Load extends ClientWorldEvent {
		public Load(World world) {
			super(world);
		}
	}

	/**
	 * Unload event.
	 */
	public static class Unload extends ClientWorldEvent {
		public Unload(World world) {
			super(world);
		}
	}

}
