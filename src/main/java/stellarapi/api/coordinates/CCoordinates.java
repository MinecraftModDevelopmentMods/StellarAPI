package stellarapi.api.coordinates;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

/**
 * Coordinates class.
 * */
public class CCoordinates {
	private final CCoordinates parent;
	private final ResourceLocation id;
	private final List<CoordElement> elements;

	CCoordinates(ResourceLocation id, @Nullable CCoordinates parent, List<CoordElement> elements) {
		this.id = id;
		this.parent = parent;
		this.elements = elements;
	}

	public @Nullable CCoordinates getParent() {
		return this.parent;
	}

	public ResourceLocation getID() {
		return this.id;
	}

	public List<CoordElement> getElements() {
		return this.elements;
	}
}
