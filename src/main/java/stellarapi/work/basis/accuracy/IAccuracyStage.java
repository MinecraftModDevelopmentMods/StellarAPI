package stellarapi.work.basis.accuracy;

import com.google.common.collect.Ordering;

/**
 * Discrete State which reflects the accuracy.
 * <code>null</code> means the unprocessed stage.
 * */
public interface IAccuracyStage<S extends IAccuracyStage> extends IAccuracyFactor<S> {

	/**
	 * Shouldn't be used externally.
	 * */
	@Deprecated
	public int compareTo(S toCompare);

}