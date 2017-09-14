package stellarapi.api.celestials.collection;

import stellarapi.api.celestials.CRequest;

public interface ICollectionPartition<P> {
	/**
	 * Get certain collection parts from the request.
	 * */
	public P[] getParts(CRequest request);

	/**
	 * Get the parent level part for specified level from specified part.
	 * */
	public P getParentLevelPart(P part, int level);

	/**
	 * Get level for certain occurrence rate.
	 * TODO need to revise
	 * */
	public int getLevelForOccurence(int occurrenceTimes);
}