package stellarapi.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import stellarapi.api.ICelestialCoordinate;

/**
 * Fired to reset the celestial coordinate. <p>
 * 
 * This event is <code>@Cancelable</code>,
 * and canceling this event will force the coordinate unavailable now.
 * */
@Cancelable
public class ResetCoordinateEvent extends PerWorldEvent {

	private ICelestialCoordinate coordinate = null;
	
	public ResetCoordinateEvent(World world) {
		super(world);
	}
	
	public ICelestialCoordinate getCoordinate() {
		return this.coordinate;
	}
	
	public void setCoordinate(ICelestialCoordinate coordinate) {
		this.coordinate = coordinate;
	}
}
