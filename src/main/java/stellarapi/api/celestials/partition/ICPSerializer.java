package stellarapi.api.celestials.partition;

import java.nio.ByteBuffer;

/**
 * Serializer for specific type.
 * */
public interface ICPSerializer<T> {
	/** Deserialize from the buffer. */
	public Iterable<T> deserialize(ByteBuffer buffer);

	/** Measures the size of specific content. */
	public int size(Iterable<T> content);

	/** Serialize to the buffer. */
	public void serialize(Iterable<T> content, ByteBuffer buffer);
}