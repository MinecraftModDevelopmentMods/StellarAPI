package stellarapi.api.coordinates;

import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface ICoordHandler {

	/**
	 * Instantiate the coordinates, adding a few if necessary.
	 * Fill in the map on the second parameter.
	 * Only put instances to override, the rest will be automatically filled up.
	 * */
	public void instantiate(IForgeRegistry<CCoordinates> registry, Map<ResourceLocation, CCoordInstance> instancesToFill);

	public Object generateSettings();
	public boolean overrideSettings(CCoordinates coordinate);

}