package stellarapi.work.basis.accuracy;

import com.google.common.collect.Ordering;

/**
 * Discrete State which reflects the accuracy.
 * <code>null</code> means the unprocessed stage.
 * */
public abstract class IAccuracyStage<S extends IAccuracyStage> implements IAccuracyFactor<S> {

	public static <X extends IAccuracyStage> Ordering<X> getOrdering() {
		return Ordering.natural().nullsFirst();
	}

	/**
	 * Shouldn't be used externally.
	 * */
	@Deprecated
	public abstract int compareTo(S toCompare);

}