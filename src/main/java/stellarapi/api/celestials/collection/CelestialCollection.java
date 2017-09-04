package stellarapi.api.celestials.collection;

import net.minecraft.world.World;

/**
 * Celestial Collection containing the data of celestial objects.
 * TODO what would this contain.
 *  This will represent the data set provided. So how would it be exposed.
 * */
public abstract class CelestialCollection {

	private long updatePeriod; // -1L for infinity
	private boolean isUpdatePeriodVariable;

	public abstract ICollectionAdaption adaption(World world);

}