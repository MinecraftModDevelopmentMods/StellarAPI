package stellarapi.api.celestials;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Ordering;

public interface IEffectorType {
	/**
	 * Celestial light source, notably Sun.
	 */
	public static IEffectorType Light = new IEffectorType() {
		@Override
		public Ordering<CelestialObject> getOrderingFor(List<CelestialObject> objects) {
			return Ordering.from(new Comparator<CelestialObject>() {
				@Override
				public int compare(CelestialObject obj1, CelestialObject obj2) {
					return Double.compare(obj1.getStandardMagnitude(), obj2.getStandardMagnitude());
				}
			});
		}

		public double effectPriority() {
			return 100.0;
		}

		@Override
		public int hashCode() {
			return 0;
		}
	};

	/**
	 * Celestial objects giving tidal effects, notably Moon.
	 * <p>
	 * Note that tide does got affected by phase(on brightness), but not in same
	 * tendency.
	 */
	public static IEffectorType Tide = new IEffectorType() {
		@Override
		public Ordering<CelestialObject> getOrderingFor(List<CelestialObject> objects) {
			return Ordering.explicit(objects);
		}

		public double effectPriority() {
			return 10.0;
		}

		@Override
		public int hashCode() {
			return 1;
		}
	};

	/**
	 * Gets Default ordering for this effector type with certain object list.
	 * 
	 * @param objects
	 *            the celestial effector object list.
	 */
	public Ordering<CelestialObject> getOrderingFor(List<CelestialObject> objects);

	/**
	 * Gets effector priority. The bigger this method returns, the prior this
	 * effect will be.
	 */
	public double effectPriority();

	@Override
	public int hashCode();
}
