package stellarapi.api.celestials.collection;

import java.util.List;

import stellarapi.api.celestials.CelestialType;

/**
 * Adaption on world.
 * */
public interface ICollectionAdaption<P, Pn> {
	/** Gets the parent collection */
	public CelestialCollection<P, Pn> getCollection();

	/** Gets the segments */
	public Iterable<ISegment> getSegments(List<P> parts);

	/**
	 * Check if certain object with rough description from this collection and
	 *  the one with detailed description from child type matches
	 * */
	public boolean checkEqual(ISegment rough, CelestialType detailedType, ISegment detailed);
	
}
