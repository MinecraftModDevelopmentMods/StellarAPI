package stellarapi.api.lib.config;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

/**
 * Describes expansion of a type. <p>
 * Note that only specified generic types(array/class) without variable arguments
 *  are accepted for configuration. <p>
 * 
 * gives illegal argument exception when the key is wrong.
 * */
public interface ITypeExpansion {
	public boolean hasKey(Object collection, String key);

	/** This should be either static or has iterator supporting remove action. */
	public Iterable<String> getKeys(Object collection);

	@Nonnull
	public Object getValue(Object collection, String key);
	/** Returns third parameter(value to set), exist for simulated singletons. */
	public Object setValue(Object collection, String key, Object value);
	public void remove(Object collection, String key);

	/**
	 * Generate an appropriate key from a suggested key.
	 * */
	public String adaptiveKey(Object collection, String suggestedKey);

	/**
	 * Reorder this object based on the key. Should only be applied for collections.
	 * The components of the list should be exactly the same with the instance keys.
	 * */
	public void reorder(Object collection, List<String> order);


	//public Object getDefault(String key);

	public static SingletonExpansion SINGLETON = new SingletonExpansion();

	public static class SingletonExpansion implements ITypeExpansion {
		private Set<String> keys = ImmutableSet.of("");
		private Set<String> empty = ImmutableSet.of();

		@Override
		public Iterable<String> getKeys(Object instance) {
			return instance != null? this.keys : this.empty;
		}

		@Override
		public Object getValue(Object instance, String key) {
			if(!key.isEmpty())
				throw new IllegalArgumentException(
						"Key expected to be empty, but got " + key + ".");
			return instance;
		}

		@Override
		public Object setValue(Object instance, String key, Object value) {
			if(!key.isEmpty())
				throw new IllegalArgumentException(
						"Key expected to be empty, but got " + key + ".");
			return value;
		}

		@Deprecated
		@Override
		public void remove(Object instance, String key) { }

		@Override
		public boolean hasKey(Object instance, String key) {
			return instance != null && key.isEmpty();
		}

		@Override
		public String adaptiveKey(Object instance, String suggestedKey) {
			return ""; // Shouldn't be called now. Though leaving this for future.
		}

		@Override
		public void reorder(Object instance, List<String> order) {
			throw new UnsupportedOperationException(
					"Reorder operation is not allowed for a singleton.");
		}
	}
}