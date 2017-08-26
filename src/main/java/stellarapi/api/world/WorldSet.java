package stellarapi.api.world;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class WorldSet extends IForgeRegistryEntry.Impl<WorldSet> {

	/** World names definitely have */
	private String[] worldNames;
	private CPriority priority;

	public abstract boolean containsWorld(World world);

	public String[] getWorldNames() {
		return this.worldNames;
	}

}
