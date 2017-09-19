package stellarapi.api.coordinates;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
	private ICoordElement[] elements = null;
	private boolean parentAvailable = false;

	/**
	 * For coordinates which will be overriden.
	 * This limits the possible selection of coordinates handler.
	 * */
	public CCoordinates() { }

	/**
	 * Declares coordinates with its default parent id.
	 * */
	public CCoordinates(ResourceLocation defaultParentID) {
		this.parentID = defaultParentID;
	}

	public void setDefaultElements(ICoordElement[] elements) {
		this.elements = elements;
	}

	public ICoordElement[] getDefaultElements() {
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
	public void injectParent(ResourceLocation parentID, CCoordinates parent) {
		this.parent = parent;
		this.parentID = parentID;
		this.parentAvailable = true;
	}

	/**
	 * Generates the settings.
	 * Only viable if there's world-specific element.
	 * */
	public @Nullable ICoordSettings generateSettings() { return null; }

	/**
	 * Apply the given settings to the specified world.
	 * */
	public void applySettings(ICoordSettings settings, World world) { }


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CCoordinates)
			return delegate.equals(((CCoordinates) obj).delegate);
		return false;
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}
}