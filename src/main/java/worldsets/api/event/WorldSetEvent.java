package worldsets.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import worldsets.api.WorldSet;

public class WorldSetEvent extends Event {
	public final WorldSet worldSet;

	protected WorldSetEvent(WorldSet worldSet) {
		this.worldSet = worldSet;
	}

	/**
	 * World Initialization.
	 * World Set could be null here.
	 * */
	public static class WorldInitializeEvent extends WorldSetEvent {
		public final World world;

		public WorldInitializeEvent(World world, WorldSet worldSet) {
			super(worldSet);
			this.world = world;
		}
	}

	// TODO WorldSets specify
}
