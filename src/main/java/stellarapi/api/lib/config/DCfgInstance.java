package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import stellarapi.api.lib.config.DynamicConfig.Default;

/**
 * Container for the information about the configuration.
 * */
public final class DCfgInstance {

	private static final Function<Field, Integer> priority = new Function<Field, Integer>() {
		@Override
		public Integer apply(Field input) {
			if(!input.isAnnotationPresent(DynamicConfig.Priority.class))
				return 0;
			return input.getAnnotation(DynamicConfig.Priority.class).value();
		}
	};

	Object instance;
	ImmutableSortedSet<Field> fieldsSet;
	ImmutableMap<Field, FieldInformation> infoMap;

	DCfgInstance(Class<?> cls, Object instance) {
		this.instance = instance;

		ImmutableSortedSet.Builder<Field> setBuilder = ImmutableSortedSet.orderedBy(
				Ordering.<Integer>natural().reverse().onResultOf(priority));
		ImmutableMap.Builder<Field, FieldInformation> mapBuilder = ImmutableMap.builder();

		for(Field field : cls.getDeclaredFields()) {
			if (!Modifier.isPublic(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()) != (instance == null))
				continue;
			if (Modifier.isTransient(field.getModifiers()))
				continue;

			setBuilder.add(field);

			FieldInformation information = new FieldInformation(
					field.getGenericType(), field.getAnnotations());

			DynamicConfig.Default def = field.getAnnotation(DynamicConfig.Default.class);
			DynamicConfig.Defaults defs = field.getAnnotation(DynamicConfig.Defaults.class);
			DynamicConfig.Default[] defArray = (defs != null)? defs.value() :
				(def != null)? new DynamicConfig.Default[] {def} : null;
			
			if(isDefaultAble(field.getGenericType(), defArray)) {
				Object value = null;

				try {
					value = field.get(instance);
				} catch (Exception exception) {
					Throwables.propagate(exception); //Can't reach here
				}

				if(DCfgManager.isPrimitive(field.getGenericType()) || instance == null) {
					information.defaultValue = value;
				} else {
					ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
					populate(field.getGenericType(), value, "", builder, defArray);
					information.defaultValues = builder.build();
				}
			}

			mapBuilder.put(field, information);
		}

		this.fieldsSet = setBuilder.build();
	}

	private static boolean isDefaultAble(Type type, DynamicConfig.Default[] defaults) {
		if(DCfgManager.isPrimitive(type) || DCfgManager.hasDefault(type, defaults))
			return true;
		if(!DCfgManager.hasExpansion(type))
			return false;

		ITypeExpansion expansion = DCfgManager.getExpansion(type);
		Type[] subTypes = expansion.getValueTypes();
		if(subTypes == null)
			return false;

		for(Type subType : subTypes)
			if(!isDefaultAble(subType, defaults))
				return false;

		return true;
	}

	private static void populate(Type type, Object value, String current,
			ImmutableMap.Builder<String, Object> builder, DynamicConfig.Default[] defaults) {
		if(DCfgManager.hasDefault(type, defaults))
			builder.put(current, DCfgManager.evaluateDefault(type, defaults));

		if(!DCfgManager.hasExpansion(type)) {
			builder.put(current, value);
			return;
		}

		ITypeExpansion expansion = DCfgManager.getExpansion(type);
		for(String key : expansion.getKeys(value)) {
			Type subType = expansion.getValueType(value, key);
			Object subValue = expansion.getValue(value, key);
			populate(subType, subValue, current + DCfgManager.SEPARATOR + key, builder, defaults);
		}
	}

	static final class FieldInformation {
		final Type type;
		final Annotation[] annotations;

		FieldInformation(Type type, Annotation[] annotations) {
			this.type = type;
			this.annotations = annotations;
		}

		Object defaultValue;
		ImmutableMap<String, Object> defaultValues;

		/*
		 * Sub-Instances, which will be populated and dealt with during synchronization.
		 * Behaves like cache.
		 */
		Map<String, DCfgInstance> subInstances;

		// Cache for property/category
		boolean isProperty;

		// Cache for checked flag
		boolean checked;
	}
}