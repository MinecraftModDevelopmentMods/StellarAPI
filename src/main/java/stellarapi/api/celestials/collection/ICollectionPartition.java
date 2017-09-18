package stellarapi.api.celestials.collection;

import java.util.List;

import stellarapi.api.celestials.CRequest;

public interface ICollectionPartition<P, Pn> {
	public List<P> parts(CRequest request, Pn partition);
}
