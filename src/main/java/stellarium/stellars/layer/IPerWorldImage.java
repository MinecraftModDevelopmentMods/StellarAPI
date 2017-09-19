package stellarium.stellars.layer;

import stellarapi.api.CelestialPeriod;

public interface IPerWorldImage<Obj extends StellarObject> extends ICelestialObject {
	
	public void initialize(Obj object, ICelestialCoordinate coordinate, ISkyEffect sky, CelestialPeriod yearPeriod);
	public void updateCache(Obj object, ICelestialCoordinate coordinate, ISkyEffect sky);

}
