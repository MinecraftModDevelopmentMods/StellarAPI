package stellarapi.work.basis.accuracy;

import com.google.common.collect.Ordering;

public class AccuracyHelper {
	public static <X extends IAccuracyStage> Ordering<X> getOrdering() {
		return Ordering.natural().nullsFirst();
	}
}