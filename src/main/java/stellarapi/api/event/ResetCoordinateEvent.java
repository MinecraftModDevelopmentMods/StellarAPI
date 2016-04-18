package stellarapi.api.event;

import net.minecraft.world.World;
import stellarapi.api.ICelestialCoordinate;

/**
 * Fired to reset the celestial coordinate.
 * */
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
