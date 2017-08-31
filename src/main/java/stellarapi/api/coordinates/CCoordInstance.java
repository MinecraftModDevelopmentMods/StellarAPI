package stellarapi.api.coordinates;

import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

public class CCoordInstance implements Comparable {
	private ResourceLocation name;
	private CCoordInstance parent;

	private CCoordinates origin = null;
	private ICoordinateElement[] elements = null;
	private boolean overriden = false;

	public CCoordInstance(CCoordinates origin, CCoordInstance parent) {
		this(origin.getRegistryName(), parent);
		this.origin = origin;
		this.elements = origin.getDefaultElements();
	}

	public CCoordInstance(ResourceLocation name, CCoordInstance parent) {
		this.name = name;
		this.parent = parent;
	}

	public static CCoordInstance of(CCoordinates origin, Map<ResourceLocation, CCoordInstance> instances) {
		if(origin.equals(DefaultCoords.base))
			return new CCoordInstance(origin, null);

		if(instances.containsKey(origin.getDefaultParentID()))
			return new CCoordInstance(origin, instances.get(origin.getDefaultParentID()));
		else throw new IllegalArgumentException("Parent not initialized");
	}

	public boolean isRoot() {
		return this.parent == null;
	}


	public ICoordinateElement[] getCoordElements() {
		return this.elements;
	}

	public ICoordinateElement getCoordElement(int index) {
		return this.elements[index];
	}

	public int numElements() {
		return elements.length;
	}

	public CCoordInstance setCoordElements(ICoordinateElement[] newElements) {
		this.elements = newElements;
		this.overriden = true;
		return this;
	}

	public CCoordInstance setCoordElement(int index, ICoordinateElement element) {
		this.elements[index] = element;
		return this;
	}


	@Nullable
	public ICoordSettings generateSettings() {
		if(this.origin == null || this.overriden)
			return null;
		for(ICoordinateElement element : this.elements)
			if(element.requiredContextTypes().contains(EnumContextType.WORLD))
				return origin.generateSettings();

		return null;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CCoordInstance) {
			CCoordInstance other = (CCoordInstance) obj;
			return name.equals(other.name) && parent.equals(other.parent);
		} else if(obj instanceof ResourceLocation) {
			return name.equals(obj);
		} else return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public int compareTo(Object obj) {
		if(obj instanceof CCoordInstance) {
			CCoordInstance other = (CCoordInstance) obj;
			if(this == other)
				return 0;
			else if(this.isDescendant(other))
				return -1;
			else if(other.isDescendant(this))
				return 1;
			return name.toString().compareTo(other.name.toString());
		}

		return 0;
	}

	private boolean isDescendant(CCoordInstance toCheck) {
		if(toCheck == null)
			return false;
		if(toCheck == this)
			return true;
		return this.isDescendant(toCheck.parent);
	}
}
