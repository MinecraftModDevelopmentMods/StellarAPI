package stellarapi.api.coordinates;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.lib.math.Vector3;

/**
 * Context class to gain the coordinates.
 * */
public class CoordContext {
	private final World world;
	private long time;
	private ICapabilityProvider theProvider = null;
	private Vector3 worldPos = null;

	/**
	 * Creates the context with the current world.
	 * TODO is +1 on time needed?
	 * */
	public CoordContext(World world) {
		this.world = world;
		this.time = world.getWorldTime();
	}

	/**
	 * Sets the time. Usually to the one which does not match the current world time.
	 * */
	public CoordContext setTime(long time) {
		this.time = time;
		return this;
	}

	public CoordContext setPosition(ICapabilityProvider theProvider, Vector3 worldPos) {
		this.theProvider = theProvider;
		this.worldPos = worldPos;
		return this;
	}

	public World getWorld() {
		return this.world;
	}

	public Object getMainSettings() {
		// TODO stub
		return null;
	}

	public Object getSpecificSettings(CCoordinates coordinates) {
		return null;
	}

	public long getTime() {
		return this.time;
	}

	public boolean supportContext(EnumContextType type) {
		switch(type) {
		case WORLD:
			return true;
		case TIME:
			return true;
		case POSITION:
			return this.worldPos != null;
		default:
			return false;
		}
	}
}