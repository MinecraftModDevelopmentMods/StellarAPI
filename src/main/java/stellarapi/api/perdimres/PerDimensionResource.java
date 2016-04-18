package stellarapi.api.perdimres;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.StellarAPIReference;

/**
 * Per dimension resource. <p>
 * Note that resource is only available on client,
 *  thus {@link PerDimensionResource#getLocation()} should only be called on client.
 * Also it is recommended not to cache the location, since the location can be changed dynamically.
 * */
public class PerDimensionResource {
	
	private String resourceId;
	private ResourceLocation defaultLocation;
	
	public PerDimensionResource(String resourceId, ResourceLocation defaultLocation) {
		this.resourceId = resourceId;
		this.defaultLocation = defaultLocation;
	}
	
	public ResourceLocation getLocation() {
		return StellarAPIReference.getLocation(this.resourceId, this.defaultLocation);
	}

}
