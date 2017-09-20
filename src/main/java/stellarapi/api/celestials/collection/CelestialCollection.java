package stellarapi.api.celestials.collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Celestial Collection containing the data of celestial objects.
 * Can be deserialized several times on systems on individual worlds.
 * 
 * Also identifier of collection settings which is, usually, applied on the collections.
 * */
public abstract class CelestialCollection<P, Pn> implements INBTSerializable<NBTTagCompound> {

	private long updatePeriod; // -1L for infinity, 0L for every render ticks
	private boolean isUpdatePeriodVariable;
	private EnumMaxIntensity maxIntensity;

	/** Maximum intensity level. */
	public static enum EnumMaxIntensity {
		STELLAR, PLANETARY, LOCALSTAR
	}

	/** Gets the adaption on world */
	public abstract ICollectionAdaption<P, Pn> adaption(World world);

	public void setupPartial() { }
	public void setupComplete() { }

	/**
	 * Checks if this collection can handle vanilla case.
	 * @param the world to handle as vanilla
	 * */
	public boolean canHandleVanilla(World world) {
		return false;
	}

	/**
	 * Gets the vanilla counterpart for this collection.
	 * Should return valid collection if {@link #canHandleVanilla(World)} gives <code>true</code>.
	 * @param world the world to handle as vanilla
	 * */
	public CelestialCollection<P, Pn> getVanillaCollection(World world) {
		throw new IllegalStateException("This collection does not support vanilla counterpart.");
	}
}