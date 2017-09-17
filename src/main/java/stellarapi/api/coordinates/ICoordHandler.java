package stellarapi.api.coordinates;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

/**
 * Coordinates handler.
 * */
public interface ICoordHandler {
	/**
	 * Handles the case where server-side object is absent. (e.g. vanila)
	 * return false if this should be replaced with a default(vanilla) provider.
	 * Those which fills additional coordinates should be able to handle vanilla on its own.
	 * */
	public boolean handleVanilla();

	/**
	 * Instantiate the coordinates, adding a few if necessary.
	 * Fill in the map on the second parameter.
	 * Only put instances to override, the rest will be automatically filled up.
	 * */
	public void instantiate(IForgeRegistry<CCoordinates> registry, Map<ResourceLocation, CCoordInstance> instancesToFill);
}