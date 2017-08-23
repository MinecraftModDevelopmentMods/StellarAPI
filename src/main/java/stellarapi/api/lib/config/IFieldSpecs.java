package stellarapi.api.lib.config;

import java.lang.reflect.Field;

import javax.annotation.Nullable;

public interface IFieldSpecs<T> {
	/** Checks if this field is accpeted or not. */
	public boolean accept(@Nullable Field field);

	/** Gets the field-specific object stored in this container. */
	public T getObject(@Nullable Field field);


	public static final SimpleSpecs NULLSPECS = new SimpleSpecs(null);

	public static class SimpleSpecs<T> implements IFieldSpecs<T> {
		private T contained;
		public SimpleSpecs(T contained) { this.contained = contained; }
		@Override
		public boolean accept(Field field) { return true; }
		@Override
		public T getObject(Field field) { return this.contained; }
	}
}