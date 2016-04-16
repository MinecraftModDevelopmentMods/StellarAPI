package stellarapi.api.perdimension;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Per-dimension resource manager. Pretty straightforward.
 * */
public class PerDimensionResourceManager {
	private List<IPerDimensionResourceHandler> listGetters = Lists.newArrayList();
	
	public void register(IPerDimensionResourceHandler handler) {
		listGetters.add(handler);
	}
	
	public ResourceLocation getLocation(World world, String resourceId, ResourceLocation defaultLocation) {
		ResourceLocation location = defaultLocation;
		for(IPerDimensionResourceHandler handler : this.listGetters)
			if(handler.accept(world, resourceId, location))
				location = handler.getLocation(world, resourceId, location);
		
		return location;
	}
}
