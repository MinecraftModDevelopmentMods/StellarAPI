package stellarapi.api.celestials.collection;

import com.google.gson.JsonObject;

public interface ICPartitionType<P, Pn> {

	/**
	 * Creates partition from the specified data.
	 * */
	public ICollectionPartition<P, Pn> createPartition(JsonObject data);

	/**
	 * Gets the basis of specified partitions.
	 * */
	public Pn basis(Pn... partitions);
}