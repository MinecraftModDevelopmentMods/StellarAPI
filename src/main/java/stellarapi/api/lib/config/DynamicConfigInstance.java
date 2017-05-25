package stellarapi.api.lib.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

/**
 * Container for the information about the configuration.
 * */
public class DynamicConfigInstance {

	private static final Function<Field, Integer> priority = new Function<Field, Integer>() {
		@Override
		public Integer apply(Field input) {
			if(!input.isAnnotationPresent(DynamicConfig.Priority.class))
				return 0;
			return input.getAnnotation(DynamicConfig.Priority.class).value();
		}
	};

	private Object instance;
	private ImmutableSortedSet<Field> fieldsSet;
	private ImmutableMap<Field, FieldInformation> infoMap;

	DynamicConfigInstance(Class<?> cls, Object instance) {
		this.instance = instance;

		ImmutableSortedSet.Builder<Field> builder = ImmutableSortedSet.orderedBy(
				Ordering.<Integer>natural().reverse().onResultOf(priority));

		for(Field field : cls.getDeclaredFields()) {
			if (!Modifier.isPublic(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()) != (instance == null))
				continue;
			if (Modifier.isTransient(field.getModifiers()))
				continue;

			
		}

		this.fieldsSet = builder.build();
	}

	private static final class FieldInformation {
		
	}
}