package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * Container for the information about the configuration.
 * */
public final class DConfigNode {

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

	DConfigNode(Class<?> cls, Object instance) {
		this.instance = instance;

		ImmutableSortedSet.Builder<Field> setBuilder = ImmutableSortedSet.orderedBy(
				Ordering.<Integer>natural().reverse().onResultOf(priority));
		ImmutableMap.Builder<Field, FieldInformation> mapBuilder = ImmutableMap.builder();

		for(Field field : cls.getDeclaredFields()) {
			// Check for validity
			if (!Modifier.isPublic(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()) != (instance == null))
				continue;
			if (Modifier.isTransient(field.getModifiers()))
				continue;

			//Put information into the sets and the maps.
			setBuilder.add(field);
			FieldInformation fieldInfo = new FieldInformation(field.getAnnotations());
			mapBuilder.put(field, fieldInfo);

			fieldInfo.isLeaf = DCfgManager.isLeaf(field);

			// Find field value
			Object fieldValue = null;			
			try {
				fieldValue = field.get(instance);
			} catch (Exception exception) {
				Throwables.propagate(exception); //Can't reach here
			}

			if(fieldValue == null)
				continue;

			//Attain Default Information
			// TODO move to the specific default handler.
			DynamicConfig.Default defInfo = field.getAnnotation(DynamicConfig.Default.class);

			if(!DCfgManager.hasExpansion(field)) {
				SubNode node = new SubNode();

				if(fieldInfo.isLeaf) {
					node.value = fieldValue;
					node.defaultValue = defInfo == null? fieldValue : DCfgManager.evaluateDefault(field, "", defInfo);
				} else if(fieldValue != null)
					node.instance = new DConfigNode(field.getType(), fieldValue);

				fieldInfo.subNodes = ImmutableMap.of("", node);
			} else {
				ITypeExpansion expansion = DCfgManager.getExpansion(field);
				fieldInfo.subNodes = Maps.newHashMap();

				for(String key : expansion.getKeys(fieldValue)) {
					Object subValue = expansion.getValue(instance, key);

					fieldInfo.create(key, subValue);
				}
			}
		}

		this.fieldsSet = setBuilder.build();
		this.infoMap = mapBuilder.build();
	}

	static final class FieldInformation {
		private final Annotation[] annotations;
		/*
		 * Values which can be only assigned to the leaf node.
		 * */
		private Map<String, SubNode> subNodes = null;

		// Cache for leaf
		private boolean isLeaf;

		// Changed flag
		private boolean isChanged;

		FieldInformation(Annotation[] annotations) {
			this.annotations = annotations;
		}

		public Annotation[] getAnnotations() {
			return this.annotations;
		}

		public boolean isLeaf() {
			return this.isLeaf;
		}

		public boolean isChanged() {
			return this.isChanged;
		}

		public boolean hasKey(String key) {
			assert(this.subNodes != null);
			return subNodes.containsKey(key);
		}

		public Set<String> keys() {
			assert(this.subNodes != null);
			return subNodes.keySet();
		}

		public void create(String key, Object subValue) {
			SubNode node = new SubNode();

			//If it's the leaf
			if(this.isLeaf) {
				node.value = subValue;
				//node.defaultValue = defInfo == null? subValue : DCfgManager.evaluateDefault(field, key, defInfo);
			} else {
				//If there are subEntries
				node.instance = new DConfigNode(subValue.getClass(), subValue);
			}
		}

		public void remove(String key) {
			assert(this.subNodes != null);
			subNodes.remove(key);
		}

		public DConfigNode getInstance(String key) {
			assert(this.subNodes != null);
			assert(!this.isLeaf);
			return subNodes.get(key).instance;
		}
	}

	static final class SubNode {
		Object value = null;
		Object defaultValue = null;
		// For non-leaves.
		DConfigNode instance = null;
	}
}