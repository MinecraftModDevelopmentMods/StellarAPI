package stellarapi.api.celestials.partition;

import com.google.gson.JsonObject;

/** Certain Partition Types */
public interface ICPartitionType<P, Pn> {
	/**
	 * Creates partition from the specified data.
	 * */
	public ICollectionPartition<P, Pn> createPartition(JsonObject data);

	/**
	 * Creates storage for specific data set.
	 * */
	public <T> ICPStorage<P, Pn, T> createStorage(Pn partition, ICPSerializer<T> serializer);

	/**
	 * Gets the basis of specified partitions.
	 * */
	public Pn basis(Pn... partitions);
}