package stellarapi.api.celestials.dispatcher;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.base.Predicate;

import stellarapi.api.celestials.CRequest;
import stellarapi.api.celestials.CelestialObject;
import stellarapi.api.celestials.CelestialType;
import stellarapi.api.celestials.collection.ICollectionAdaption;
import stellarapi.api.celestials.collection.ISegment;
import stellarapi.api.celestials.collection.model.TypeModel;
import stellarapi.api.celestials.partition.ICollectionPartition;

public class CelestialDispatcher {
	public void iterateOnObjects(CRequest request,
			Predicate<ISegment> procSegmentOnly,
			Consumer<DispatchEntry> entryConsumer) {
		
	}

	private <P,Pn> void iterate(CRequest request,
			CelestialType type, 
			ICollectionPartition<P,Pn> partition,
			ICollectionAdaption<P,Pn> adaption,
			Predicate<ISegment> procSegmentOnly,
			Consumer<DispatchEntry> entryConsumer) {

		List<P> parts = partition.getParts(request, adaption.partitionFor(request));
		TypeModel model = type.generateTypeModel();
		DispatchEntry entry = new DispatchEntry();

		for(ISegment segment : adaption.getSegments(parts)) {
			// TODO CelestialDispatcher write up logic here

			entry.setTypeSegment(type, segment);
			if(procSegmentOnly.apply(segment)) {
				entryConsumer.accept(entry);
				continue;
			}

			model.readFrom(segment, request);
			entry.setModel(model);

			CelestialObject object = model.check(segment);
			if(object == null) {
				entryConsumer.accept(entry);
				continue;
			}

			entry.setObject(object);
		}
	}
}
