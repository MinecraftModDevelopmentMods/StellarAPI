package stellarapi.api.coordinates;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Coordinates handler.
 * 
 * Also identifier of main coordinates settings which is applied on the coordinates.
 * It is advised to apply changes on this handler, otherwise things need to be synchronized manually.
 * */
public interface ICoordHandler extends INBTSerializable<NBTTagCompound> {
	/**
	 * Handles the case where server-side object is absent. (e.g. vanila)
	 * This changes this handler and world-based objects to fit in vanilla case.
	 * Those which fills additional coordinates should be able to handle vanilla on its own.
	 * 
	 * This is called before apply settings event on coordinates, so take care of it.
	 * @param world the world this handler is on
	 * @return <code>false</code> iff. this should be replaced with a default(vanilla) provider.
	 * */
	public boolean handleVanilla(World world);

	/**
	 * Instantiate the coordinates, adding a few if necessary.
	 * Fill in the map on the second parameter.
	 * Only put instances to override, the rest will be automatically filled up.
	 * */
	public void instantiate(IForgeRegistry<CCoordinates> registry, Map<ResourceLocation, CCoordInstance> instancesToFill);
}