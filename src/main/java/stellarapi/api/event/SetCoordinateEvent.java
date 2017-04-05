package stellarapi.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.ICelestialCoordinate;

/**
 * Fired to set the celestial coordinate.
 * <p>
 * 
 * This event is <code>@Cancelable</code>, and canceling this event will force
 * the coordinate unavailable now.
 * 
 * TODO implement waiting for packet as world update.
 */
@Cancelable
public class SetCoordinateEvent extends PerWorldEvent {

	private ICelestialCoordinate coordinate = null;

	public SetCoordinateEvent(World world) {
		super(world);
	}

	public ICelestialCoordinate getCoordinate() {
		return this.coordinate;
	}

	public void setCoordinate(ICelestialCoordinate coordinate) {
		this.coordinate = coordinate;
	}
}
