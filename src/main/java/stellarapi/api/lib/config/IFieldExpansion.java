package stellarapi.api.lib.config;

import java.lang.reflect.Type;

import javax.annotation.Nonnull;

/**
 * Field wrapper.
 * */
public interface IFieldExpansion {
	/**
	 * Type of the values contained in this field.
	 * */
	public Type getValueType();

	public String[] getKeys();

	@Nonnull
	public Object getValue(String key);
	public Type getValueType(String key);
	public void setValue(String key, Object value);
	public void remove(String key);

	public boolean hasKey(String key);
}