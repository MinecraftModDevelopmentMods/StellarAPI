package stellarapi.api.world.resource;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Per-dimension resource manager. Pretty straightforward.
 */
public class WorldResourceManager {
	private List<IWorldResourceHandler> listGetters = Lists.newArrayList();

	public void register(IWorldResourceHandler handler) {
		listGetters.add(handler);
	}

	public ResourceLocation getLocation(World world, String resourceId, ResourceLocation defaultLocation) {
		ResourceLocation location = defaultLocation;
		for (IWorldResourceHandler handler : this.listGetters)
			if (handler.accept(world, resourceId, location))
				location = handler.getLocation(world, resourceId, location);

		return location;
	}
}
