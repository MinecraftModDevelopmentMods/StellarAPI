package stellarapi.api.event;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import stellarapi.api.SAPIReference;

/**
 * Superclass of per-entity events in Stellar API.
 * <p>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.
 * <p>
 * 
 * All child events of this event is fired on
 * {@link SAPIReference#getEventBus()}.
 */
public class PerEntityEvent extends Event {

	private final Entity entity;

	public PerEntityEvent(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Getter for the world instance.
	 */
	public Entity getEntity() {
		return this.entity;
	}
}
