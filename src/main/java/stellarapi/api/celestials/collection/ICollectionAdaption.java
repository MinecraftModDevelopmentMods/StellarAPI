package stellarapi.api.celestials.collection;

import java.util.List;

import stellarapi.api.celestials.CelestialType;

/**
 * Adaption on world.
 * */
public interface ICollectionAdaption<P> {
	/** Gets the parent collection */
	public CelestialCollection<P> getCollection();

	/** Gets the segments */
	public Iterable<ISegment> getSegments(List<P> parts);

	public boolean checkEqual(ISegment rough, CelestialType nextType, ISegment detailed);

	//public boolean accept(CSegment prop)?

	
}
