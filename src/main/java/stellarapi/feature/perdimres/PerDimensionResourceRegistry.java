package stellarapi.feature.perdimres;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import stellarapi.api.perdimres.IPerDimensionResourceHandler;

/**
 * Registry for per-dimension resources.
 */
public enum PerDimensionResourceRegistry implements IPerDimensionResourceHandler {
	INSTANCE;

	private Set<String> resourceIds = Sets.newHashSet();

	PerDimensionResourceRegistry() {
		resourceIds.add("DEBUG_ONLY");
	}

	public void registerResourceId(String id) {
		resourceIds.add(id);
	}

	public ImmutableSet<String> getResourceIds() {
		return ImmutableSet.copyOf(this.resourceIds);
	}

	@Override
	public boolean accept(World world, String resourceId, ResourceLocation previous) {
		return PerDimensionResourceData.getData(world).getResourceMap().containsKey(resourceId);
	}

	@Override
	public ResourceLocation getLocation(World world, String resourceId, ResourceLocation previous) {
		return PerDimensionResourceData.getData(world).getResourceMap().get(resourceId);
	}

}
