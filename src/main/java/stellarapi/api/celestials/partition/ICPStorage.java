package stellarapi.api.celestials.partition;

import java.util.List;

import com.google.common.collect.Multimap;

/** Storage for objects organized by these partitions */
public interface ICPStorage<P, Pn, T> {
	/** Initially loads the data. */
	public void load();
	/** Used by storage backed by memory, to write on the file. */
	public void save();

	/** Gets the objects stored in the specified parts. */
	public Iterable<T> get(List<P> parts);

	/** Puts the objects in this storage. */
	public void put(Multimap<P, T> objects);

	/**
	 * Required partition this storage needs.
	 * All parts need to be from the partition.
	 * */
	public Pn requiredPartition();
}