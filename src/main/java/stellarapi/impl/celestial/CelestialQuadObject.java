package stellarapi.impl.celestial;

import net.minecraft.util.ResourceLocation;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.EnumObjectType;
import stellarapi.api.lib.math.Vector3;
import stellarapi.api.observe.SearchRegion;

public class CelestialQuadObject extends CelestialObject {
	private final Vector3[] corners;

	public CelestialQuadObject(ResourceLocation nameIn, EnumObjectType typeIn,
			Vector3 pos, Vector3[] corners) {
		super(nameIn, typeIn);
		this.setPos(pos);
		this.corners = corners;
	}

	/**
	 * @param region region in absolute positions
	 * */
	public boolean intersects(SearchRegion region) {
		return region.doesIntersect(this.corners);
	}
}
