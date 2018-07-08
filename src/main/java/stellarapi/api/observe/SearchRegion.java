package stellarapi.api.observe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import stellarapi.api.lib.math.SpCoord;

/**
 * Search region defined as a sum of triangles on the sphere.
 * */
public class SearchRegion implements Predicate<SpCoord> {
	public final SpCoord[] coords;
	public final int[][] triangles;

	private SearchRegion(SpCoord[] coordsIn, int[][] trianglesIn) {
		this.coords = coordsIn;
		this.triangles = trianglesIn;
	}

	@Override
	public boolean test(SpCoord t) {
		// TODO AA Search IMPLEMENT THIS
		return false;
	}


	/** Creates quad search region. It should be in counterclockwise order. */
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
