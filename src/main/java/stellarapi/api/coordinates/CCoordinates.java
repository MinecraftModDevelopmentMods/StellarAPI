package stellarapi.api.coordinates;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class CCoordinates extends IForgeRegistryEntry.Impl<CCoordinates> {
	private ResourceLocation parentID = null;
	private ICoordinateElement[] elements = null;

	/**
	 * For those which will be overriden.
	 * */
	CCoordinates() { }

	/**
	 * Declares coordinates with its default parent id.
	 * */
	public CCoordinates(ResourceLocation defaultParentID) {
		this.parentID = defaultParentID;
	}

	public void setDefaultElements(ICoordinateElement[] elements) {
		this.elements = elements;
	}

	public ICoordinateElement[] getDefaultElements() {
		return this.elements;
	}

	public ResourceLocation getDefaultParentID() {
		return this.parentID;
	}

	/**
	 * Generates the settings.
	 * Only viable if there's world-specific element.
	 * */
	public ICoordSettings generateSettings() {
		return null;
	}
}