package stellarapi.api.world.resource;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.SAPIReferences;

/**
 * Per dimension resource.
 * <p>
 * Note that resource is only available on client, thus
 * {@link PerWorldResource#getLocation()} should only be called on client.
 * Also it is recommended not to cache the location, since the location can be
 * changed dynamically.
 */
public class PerWorldResource {

	private String resourceId;
	private ResourceLocation defaultLocation;

	public PerWorldResource(String resourceId, ResourceLocation defaultLocation) {
		this.resourceId = resourceId;
		this.defaultLocation = defaultLocation;
	}

	public ResourceLocation getLocation() {
		return SAPIReferences.getLocation(this.resourceId, this.defaultLocation);
	}

}
