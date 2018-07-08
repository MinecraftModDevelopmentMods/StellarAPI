package stellarapi.api.observe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import stellarapi.api.lib.math.Matrix3;
import stellarapi.api.lib.math.SpCoord;
import stellarapi.api.lib.math.Vector3;

/**
 * Search region defined as a sum of triangles on the sphere.
 * */
public class SearchRegion implements Predicate<SpCoord> {
	public final SpCoord[] coords;
	private Vector3[] positions = null;
	public final int[][] triangles;

	private SearchRegion(SpCoord[] coordsIn, int[][] trianglesIn) {
		this.coords = coordsIn;
		this.triangles = trianglesIn;
	}

	@Override
	public boolean test(SpCoord t) {
		return this.test(t.getVec());
	}

	// TODO Consider direction of the triangle & More accurate calculation for small triangles
	public boolean test(Vector3 pos) {
		if(this.positions == null) {
			this.positions = new Vector3[coords.length];
			for(int i = 0; i < coords.length; i++)
				this.positions[i] = coords[i].getVec();
		}

		Vector3 copy = new Vector3();
		for(int[] triangle : this.triangles) {
			Matrix3 conv = new Matrix3();
			for(int i = 0; i < 3; i++)
				conv.setRow(i, this.positions[triangle[i]]);
			conv.invert();
			conv.transform(copy.set(pos));
			return copy.getX() > 0.0 && copy.getY() > 0.0 && copy.getZ() > 0.0;
		}

		return false;
	}

	public boolean doesIntersect(SpCoord[] convex) {
		for(SpCoord pos : convex) {
			if(this.test(pos))
				return true;
		}
		return false;
	}

	public boolean doesIntersect(Vector3[] convex) {
		for(Vector3 pos : convex) {
			if(this.test(pos))
				return true;
		}
		return false;
	}


	/** Creates quad search region. It should be in counterclockwise order viewed from the center. */
	public static SearchRegion quad(SpCoord a, SpCoord b, SpCoord c, SpCoord d) {
		Builder builder = builder();
		builder.addPos(a).addPos(b).addPos(c).addPos(d);
		builder.addTriangle(0, 1, 2).addTriangle(2, 3, 0);
		return builder.build();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<SpCoord> coords = new ArrayList<>();
		private final List<int[]> triangles = new ArrayList<>();

		public Builder addPos(SpCoord coord) {
			coords.add(coord);
			return this;
		}

		public Builder addTriangle(int i, int j, int k) {
			triangles.add(new int[] {i, j, k});
			return this;
		}

		public SearchRegion build() {
			return new SearchRegion(coords.toArray(new SpCoord[0]),
					triangles.toArray(new int[0][]));
		}
	}
}
