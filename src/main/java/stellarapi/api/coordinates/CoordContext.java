package stellarapi.api.coordinates;

import java.util.Set;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.SAPICapabilities;
import stellarapi.api.patch.BasePatchHandler;

/**
 * Context class to gain the coordinates.
 * */
public class CoordContext {
	private final World world;
	private long time;
	private ICapabilityProvider theProvider = null;
	private Vec3d worldPos = null;

	public static ILocalCoordinates getEvaluator(CoordContext context) {
		if(BasePatchHandler.isLocationSpecific() && context.theProvider != null)
			return context.theProvider.getCapability(SAPICapabilities.LOCAL_COORDINATES, null);
		else return context.world.getCapability(SAPICapabilities.LOCAL_COORDINATES, null);
	}

	/**
	 * Creates the context with the current world.
	 * */
	public CoordContext(World world, boolean offset) {
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

	public CoordContext setPosition(ICapabilityProvider theProvider, Vec3d worldPos) {
		this.theProvider = theProvider;
		this.worldPos = worldPos;
		return this;
	}

	public World getWorld() {
		return this.world;
	}

	public long getTime() {
		return this.time;
	}

	public ICapabilityProvider getLocalProvider() {
		return this.theProvider;
	}

	public Vec3d getWorldPos() {
		return this.worldPos;
	}

	public boolean supportContext(Set<EnumContextType> typeSet) {
		for(EnumContextType type : typeSet)
			if(!this.supportContext(type))
				return false;
		return true;
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