package worldsets.api.event;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import worldsets.api.worldset.WorldSet;

public class WorldSetEvent extends Event {
	public final WorldSet worldSet;

	protected WorldSetEvent(WorldSet worldSet) {
		this.worldSet = worldSet;
	}

	// TODO WorldSets specify
}
