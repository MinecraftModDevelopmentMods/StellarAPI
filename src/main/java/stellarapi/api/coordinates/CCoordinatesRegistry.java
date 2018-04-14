package stellarapi.api.coordinates;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

/**
 * Registry for coordinates. Registry for now, it will be just a list later.
 * */
public class CCoordinatesRegistry extends RegistryNamespacedDefaultedByKey<ResourceLocation, CCoordinates> {
	public static final ResourceLocation HORIZON = new ResourceLocation("horizon");

	public CCoordinatesRegistry() {
		super(HORIZON);
		// TODO It depends on world, so what should I do? Also can be overriden.
	}
}
