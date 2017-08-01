package stellarapi.api.lib.config;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;

/**
 * Describes expansion of a type. <p>
 * Note that only specified generic types(array/class) without variable arguments
 *  are accepted for configuration. <p>
 * 
 * gives assertion exception when the key is wrong.
 * */
public interface ITypeExpansion {
	/** This should be iterable, i.e. either static or has iterator supporting remove action. */
	public Set<String> getKeys(Object instance);

	@Nonnull
	public Object getValue(Object instance, String key);
	/** Returns third parameter(value to set), exist for simulated singletons. */
	public Object setValue(Object instance, String key, Object value);
	public void remove(Object instance, String key);

	public boolean hasKey(Object instance, String key);

	//public Object getDefault(String key);

	public static class SingletonExpansion implements ITypeExpansion {
		private Set<String> keys = ImmutableSet.of("");

		@Override
		public Set<String> getKeys(Object instance) {
			return this.keys;
		}

		@Override
		public Object getValue(Object instance, String key) {
			assert(key.isEmpty());
			return instance;
		}

		@Override
		public Object setValue(Object instance, String key, Object value) {
			assert(key.isEmpty());
			return value;
		}

		@Deprecated
		@Override
		public void remove(Object instance, String key) { }

		@Override
		public boolean hasKey(Object instance, String key) {
			return key.isEmpty();
		}
		
	}
}