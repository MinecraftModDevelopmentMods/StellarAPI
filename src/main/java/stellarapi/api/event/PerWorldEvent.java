package stellarapi.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import stellarapi.api.SAPIReference;

/**
 * Superclass of per-world(dimension) events in Stellar API.
 * <p>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.
 * <p>
 * 
 * All child events of this event is fired on
 * {@link SAPIReference#getEventBus()}.
 */
public class PerWorldEvent extends Event {

	private final World world;

	public PerWorldEvent(World world) {
		this.world = world;
	}

	/**
	 * Getter for the world instance.
	 */
	public World getWorld() {
		return this.world;
	}
}
