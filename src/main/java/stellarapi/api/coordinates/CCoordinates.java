package stellarapi.api.coordinates;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

/**
 * Coordinates, as reference posture of the objects.
 * Declaration of the coordinates could be overrode by coordinates handler.
 * Check for existence before registration, or duplication can happen!
 * 
 * Default one is the celestial one.
 * TODO injection of coordinates - this lessen the effort of system side.
 * */
public class CCoordinates extends IForgeRegistryEntry.Impl<CCoordinates> {
	private ResourceLocation parentID = null;
	private CCoordinates parent = null;
	private ICoordinateElement[] elements = null;
	private boolean parentAvailable = false;

	/**
	 * For those which will be overriden.
	 * Don't call this!
	 * */
	@Deprecated
	public CCoordinates() { }

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

	/** Gets default parent. Throws exception when accessed too early. */
	public CCoordinates getDefaultParent() {
		if(this.parentAvailable)
			return this.parent;
		else throw new NullPointerException();
	}


	@Deprecated
	public void injectParent(CCoordinates parent) {
		this.parent = parent;
		this.parentID = parent != null? parent.getRegistryName() : null;
		this.parentAvailable = true;
	}

	/**
	 * Generates the settings.
	 * Only viable if there's world-specific element.
	 * */
	public ICoordSettings generateSettings() {
		return null;
	}
}