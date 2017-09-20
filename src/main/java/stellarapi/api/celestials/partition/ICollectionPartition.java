package stellarapi.api.celestials.partition;

import java.util.List;

import stellarapi.api.celestials.CRequest;

public interface ICollectionPartition<P, Pn> {
	/**
	 * Get parts from specified partition for specified request.
	 * */
	public List<P> getParts(CRequest request, Pn partition);

	/**
	 * Gets cover composed of the parts from specified partition
	 *  which covers the specified parts.
	 * */
	public List<P> cover(List<P> parts, Pn partition);

	/**
	 * Singleton version of {@link #cover(Object, Object)} as this case happens a lot.
	 * */
	public List<P> cover(P parts, Pn partition);
}
