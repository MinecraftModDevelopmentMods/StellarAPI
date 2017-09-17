package stellarapi.api.celestials.dispatcher;

import java.util.function.Consumer;

import com.google.common.base.Predicate;

import stellarapi.api.celestials.CRequest;
import stellarapi.api.celestials.collection.ICollectionAdaption;
import stellarapi.api.celestials.collection.ISegment;

public class CelestialDispatcher {
	public void iterateOnObjects(CRequest request,
			Predicate<ISegment> procSegmentOnly,
			Consumer<DispatchEntry> entryConsumer) {
		
	}

	private void iterate(ICollectionAdaption<?> adaption,
			Predicate<ISegment> procSegmentOnly,
			Consumer<DispatchEntry> entryConsumer) {
		
	}
}
