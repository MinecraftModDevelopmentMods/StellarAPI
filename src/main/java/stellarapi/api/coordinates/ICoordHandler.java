package stellarapi.api.coordinates;

import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public interface ICoordHandler {

	public void instantiate(IForgeRegistry<CCoordinates> registry, Map<ResourceLocation, CCoordInstance> instancesToFill);

}