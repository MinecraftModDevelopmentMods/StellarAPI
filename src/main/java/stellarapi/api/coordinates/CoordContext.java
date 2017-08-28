package stellarapi.api.coordinates;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stellarapi.api.lib.math.Vector3;

public class CoordContext {
	private World world;
	private long time;
	private ICapabilityProvider theProvider;
	private Vector3 worldPos;

	/**
	 * Sets the world, and sets the time to the current world time.
	 * */
	public CoordContext setWorld(World world) {
		this.world = world;
		this.time = world.getWorldTime();
		return this;
	}

	public CoordContext setTime(long time) {
		this.time = time;
		return this;
	}

	public CoordContext setPosObject(ICapabilityProvider theProvider) {
		this.theProvider = theProvider;
		return this;
	}

	public CoordContext setPosition(Vector3 worldPos) {
		this.worldPos = new Vector3(worldPos);
		return this;
	}
}