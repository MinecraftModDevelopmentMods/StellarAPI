package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * Configuration manager.
 */
public final class DCfgManager {

	// Expansions
	private static Multimap<Class<?>, IFieldSpecs<ITypeExpansion>> expansions = HashMultimap.create();
	// Leaf properties
	private static Multimap<Class<?>, IFieldSpecs<IPropertyType>> leafProperties = HashMultimap.create();
	private static Set<Class<?>> nativeTypes = Sets.newHashSet();

	static {
		// Native Types
		registerPropTypes(IPropertyType.boolT, boolean.class, Boolean.class);

		registerPropTypes(IPropertyType.byteT, byte.class, Byte.class);
		registerPropTypes(IPropertyType.charT, char.class, Character.class);
		registerPropTypes(IPropertyType.shortT, short.class, Short.class);
		registerPropTypes(IPropertyType.intT, int.class, Integer.class);
		registerPropTypes(IPropertyType.longT, long.class, Long.class);

		registerPropTypes(IPropertyType.floatT, float.class, Float.class);
		registerPropTypes(IPropertyType.doubleT, double.class, Double.class);

		registerPropTypes(IPropertyType.stringT, String.class);

		// Array Types
		registerPropTypes(IPropertyType.boolAT, boolean[].class);
		registerPropTypes(IPropertyType.BoolAT, Boolean[].class);

		registerPropTypes(IPropertyType.byteAT, byte[].class);
		registerPropTypes(IPropertyType.ByteAT, Byte[].class);
		registerPropTypes(IPropertyType.charAT, char[].class);
		registerPropTypes(IPropertyType.CharacterAT, Character[].class);
		registerPropTypes(IPropertyType.shortAT, short[].class);
		registerPropTypes(IPropertyType.ShortAT, Short[].class);
		registerPropTypes(IPropertyType.intAT, int[].class);
		registerPropTypes(IPropertyType.IntegerAT, Integer[].class);
		registerPropTypes(IPropertyType.longAT, long[].class);
		registerPropTypes(IPropertyType.LongAT, Long[].class);
		
		registerPropTypes(IPropertyType.floatAT, float[].class);
		registerPropTypes(IPropertyType.FloatAT, Float[].class);
		registerPropTypes(IPropertyType.doubleAT, double[].class);
		registerPropTypes(IPropertyType.DoubleAT, Double[].class);

		registerPropTypes(IPropertyType.stringAT, String[].class);

		// List Handling
		for(Class<?> nativeType : nativeTypes) {
			registerPropType(List.class,
					new FixedListPropertySpec(nativeType));
		}

		// Expansions
		registerExpansion(List.class, ITypeExpansion.LIST);
		registerExpansion(Map.class, new MapExpansionSpec());
	}

	private static void registerPropTypes(IPropertyType propType, Class<?>... nativeClasses) {
		for(Class<?> nativeClass : nativeClasses)
			registerPropType(nativeClass, propType);

		// Search for native types
		if(propType.alternativeIds() == null)
			for(Class<?> nativeClass : nativeClasses)
				nativeTypes.add(nativeClass);
	}

	public static void registerPropType(Class<?> type, IPropertyType propType) {
		registerPropType(type, new IFieldSpecs.SimpleSpecs(propType));
	}

	public static void registerPropType(Class<?> type, IFieldSpecs<IPropertyType> spec) {
		leafProperties.put(type, spec);
	}


	public static void registerExpansion(Class<?> type, ITypeExpansion expansion) {
		registerExpansion(type, new IFieldSpecs.SimpleSpecs(expansion));
	}

	public static void registerExpansion(Class<?> type, IFieldSpecs<ITypeExpansion> spec) {
		expansions.put(type, spec);
	}

	/**
	 * Read the config-nodes into the instance configuration.
	 * */
	private static void inputConfig(Class<?> cls, Object instance, DCfgInstanceNode node) {
		for(Field field : node.fieldsSet) {
			Object fieldValue = getFieldValue(field, instance);
			DCfgFieldNode fieldNode = node.fieldNodeMap.get(field);

			if((fieldValue == null && !fieldNode.hasOneLeaf()) || !fieldNode.isChanged())
				continue;

			// Sets config node value and propagates this one.
			ITypeExpansion expansion = fieldNode.getExpansion();

			// Instances and Nodes should be synchronized till this point.
			for(String key : fieldNode.keys()) {
				DCfgInstanceNode childNode = fieldNode.getNode(key);
				if(childNode.isLeafNode()) {
					try { // Simulates the singleton case.
						field.set(instance,
								expansion.setValue(fieldValue, key, childNode.getValue()));
					} catch (Exception exc) {
						Throwables.propagate(exc); // Can't reach here
					}
				} else {
					Object childInstance = expansion.getValue(fieldValue, key);
					inputConfig(childInstance.getClass(), childInstance, childNode);
				}
			}
		}
	}

	/**
	 * Apply dependence and restrictions to the instance and collection nodes here.
	 * */
	private static void applyDepNRestrictions(Class<?> cls, Object instance, DCfgInstanceNode node, boolean forChanged) {
		for(Field field : node.fieldsSet) {
			Object fieldValue = getFieldValue(field, instance);
			DCfgFieldNode fieldNode = node.fieldNodeMap.get(field);

			if(fieldNode.isChanged() != forChanged)
				return;

			// Update annotations before applying restrictions
			fieldNode.updateAnnotations(instance, EnumConfigPhase.ApplyRestrictions);

			if(fieldNode.hasAnnotation(DynamicConfig.Dependence.class))
				DCfgManager.applyDependence(instance, fieldValue, fieldNode);

			if(fieldValue == null && !fieldNode.hasOneLeaf())
				continue;

			ITypeExpansion expansion = fieldNode.getExpansion();

			// Applies restrictions. Only leaf nodes can get these restrictions.
			for(String key : fieldNode.keys()) {
				if(!expansion.hasKey(instance, key))
					continue;

				DCfgInstanceNode childNode = fieldNode.getNode(key);
				if(childNode.isLeafNode())
					for(Class<? extends Annotation> annType : childNode.getAnnotationTypes()) {
						Object newValue = restrictValue(expansion.getValue(fieldValue, key), annType, childNode.getAnnotation(annType));
						try {
							field.set(instance,
									expansion.setValue(fieldValue, key, newValue));
						} catch(Exception exception) {
							Throwables.propagate(exception);
						}
					}
			}

			// Apply restrictions on the collection.
			if(fieldNode.isCollection())
				for(Class<? extends Annotation> annType : fieldNode.getAnnotationTypes())
					restrictCollection(fieldValue, expansion, fieldNode, annType, fieldNode.getAnnotation(annType));

			// Propagations - around field node, due to instance.
			for(String key : fieldNode.keys()) {
				if(!expansion.hasKey(instance, key))
					continue;

				DCfgInstanceNode childNode = fieldNode.getNode(key);
				if(!childNode.isLeafNode()) {
					Object childInstance = expansion.getValue(fieldValue, key);
					applyDepNRestrictions(childInstance.getClass(), childInstance, childNode, forChanged);
				}
			}
		}
	}

	/**
	 * Resolve the discord between instance and config nodes,
	 *  and write the instance variables to the config nodes.
	 * */
	private static void finishNoutputConfig(Class<?> cls, Object instance, DCfgInstanceNode node) {
		for(Field field : node.fieldsSet) {
			Object fieldValue = getFieldValue(field, instance);
			DCfgFieldNode fieldNode = node.fieldNodeMap.get(field);
			ITypeExpansion<Object> expansion = fieldNode.getExpansion();
			List<String> trackedOrder = Lists.newArrayList();

			// Synchronizes the collections - direction depends on the mutability(configurability)
			//  If the collection is configurable, requested changes are going to be applied.
			// Also propagation is done here.
			if(fieldNode.isConfigurable() && fieldNode.isChanged()) {
				// Changed flag is for initial conditions where the nodes are prepared.
				// Especially when the collection is computed late with the dependency.
				// Here, nothing actually changed so instance and node should match.

				for(String key : fieldNode.keys()) {
					trackedOrder.add(key);
					if(fieldNode.requestedToRemove().contains(key))
						continue;

					DCfgInstanceNode childNode = fieldNode.getNode(key);

					if(!childNode.isLeafNode()) {
						Object childInstance = expansion.getValue(fieldValue, key);
						finishNoutputConfig(childInstance.getClass(), childInstance, childNode);
					}
				}

				if(!expansion.isOrderDependent()) {
					for(String keyToAdd : fieldNode.requestedToAdd()) { // Nodes are added here
						Map.Entry<String, Object> childPair = createInstance(instance, field, fieldValue, keyToAdd, fieldNode.isCollection());
						Object childInstance = childPair.getValue();
						String key = expansion.getValidKey(fieldValue, childPair.getKey(), -1);
						DCfgInstanceNode childNode = fieldNode.addRequested(instance, fieldValue, keyToAdd, key, childInstance);
						expansion.setValue(fieldValue, key, childInstance);
						if(!childNode.isLeafNode())
							childNode.setSyncNeeded(true);
					}

					for(String keyToRemove : fieldNode.requestedToRemove()) {
						Object childInstance = expansion.getValue(fieldValue, keyToRemove);
						fieldNode.removeRequested(keyToRemove);
						removeInstance(instance, field, fieldValue, keyToRemove, childInstance, fieldNode.isCollection());
					}

					for(Map.Entry<String, String> pairToChangeKey : fieldNode.requestedToChangeKey().entrySet()) {
						String oldKey = pairToChangeKey.getKey();
						String newKey = pairToChangeKey.getValue();

						fieldNode.changeKeyRequested(oldKey, newKey);
						expansion.changeKey(fieldValue, oldKey, newKey);
					}
				} else {
					for(String keyToAdd : fieldNode.requestedToAdd()) { // Nodes are added here
						Map.Entry<String, Object> childPair = createInstance(instance, field, fieldValue, keyToAdd, fieldNode.isCollection());
						Object childInstance = childPair.getValue();
						String key = expansion.getValidKey(fieldValue, childPair.getKey(), trackedOrder.size());
						DCfgInstanceNode childNode = fieldNode.addRequested(instance, fieldValue, keyToAdd, key, childInstance);
						expansion.setValue(fieldValue, key, childInstance);
						trackedOrder.add(key);
						if(!childNode.isLeafNode())
							childNode.setSyncNeeded(true);
							sync(childInstance.getClass(), childInstance, childNode);
					}

					Iterator<Map.Entry<String, Object>> instanceIte = expansion.preservedOrder(fieldValue).iterator();
					ListIterator<String> orderIte = trackedOrder.listIterator();
					while(!instanceIte.hasNext()) {
						Map.Entry<String, Object> instanceEntry = instanceIte.next();
						String orderKey = orderIte.next();
						if(fieldNode.requestedToRemove().contains(orderKey)) {
							Object childInstance = instanceEntry.getValue();
							fieldNode.removeRequested(orderKey);
							instanceIte.remove();
							orderIte.remove();
							removeInstance(instance, field, fieldValue, instanceEntry.getKey(), childInstance, fieldNode.isCollection());
						}

						if(fieldNode.requestedToChangeKey().containsKey(orderKey)) {
							String newKey = fieldNode.requestedToChangeKey().get(orderKey);
							int theIndex = trackedOrder.indexOf(newKey);
							if(theIndex >= 0)
								trackedOrder.set(theIndex, orderKey);

							fieldNode.changeKeyRequested(orderKey, newKey);
							orderIte.set(newKey);
						}
					}
				}

				// Refreshes the request and establishes the (new) order.
				fieldNode.refreshRequest();
			} else {
				for(String key : expansion.getKeys(fieldValue)) {
					Object childValue = expansion.getValue(fieldValue, key);

					if(!fieldNode.hasKey(key)) { // Added instance
						fieldNode.createNode(instance, fieldValue, key, childValue);
						DCfgInstanceNode childNode = fieldNode.getNode(key);
						if(!childNode.isLeafNode())
							childNode.setSyncNeeded(true);
					} else if(!fieldNode.getNode(key).isLeafNode()) {
						DCfgInstanceNode childNode = fieldNode.getNode(key);
						if(childValue != childNode.getInstance()) { // Reference changes
							fieldNode.onRemove(key);
							fieldNode.createNode(instance, fieldValue, key, childValue);
							fieldNode.getNode(key).setSyncNeeded(true);
						} else finishNoutputConfig(childValue.getClass(), childValue, childNode);
					}
				}

				// Tracks removed instances
				Iterator<String> iterator = fieldNode.keys().iterator();
				while(!iterator.hasNext()) {
					String key;
					if(!expansion.hasKey(fieldValue, key = iterator.next())) {
						fieldNode.onRemove(key);
						iterator.remove();
					}
				}

				// Updates the post-order to the order as changes applied.
				fieldNode.updatePostOrder();
			}

			//Reorders the collection.
			if(fieldNode.isCollection() && expansion.isOrdered()) {
				if(fieldNode.isOrderConfigurable() && fieldNode.isChanged()) {
					fieldNode.reorderInstanceField(expansion, fieldValue, trackedOrder);
				} else {
					fieldNode.reorder(fieldValue, ImmutableList.copyOf(expansion.getKeys(fieldValue)));
				}
			}

			//Sets config node value from instance.
			//Also checks are done on every fields, too. Out of if statement for just in case.
			fieldNode.processChanged();
			for(String key : expansion.getKeys(fieldValue)) {
				DCfgInstanceNode childNode = fieldNode.getNode(key);
				childNode.processChanged();
				if(childNode.isLeafNode())
					childNode.setValueFromInstance(expansion.getValue(fieldValue, key));
				else if(childNode.isSyncNeeded()) {
					Object childInstance = childNode.getInstance();
					sync(childInstance.getClass(), childInstance, childNode);
					childNode.setSyncNeeded(false);
				}
			}
		}
	}

	private static void sync(Class<?> cls, Object instance, DCfgInstanceNode node) {
		inputConfig(cls, instance, node);
		applyDepNRestrictions(cls, instance, node, false);
		applyDepNRestrictions(cls, instance, node, true);
		finishNoutputConfig(cls, instance, node);
	}

	static Object getFieldValue(Field field, Object instance) {
		try {
			return field.get(instance);
		} catch(Exception exc) {
			throw Throwables.propagate(exc); // Usually, can't reach here.
		}
	}

	private static int getPriority(Field field) {
		if(!field.isAnnotationPresent(DynamicConfig.Priority.class))
			return 0;
		return field.getAnnotation(DynamicConfig.Priority.class).value();
	}

	static boolean isCollection(Field field) {
		if(field.isAnnotationPresent(DynamicConfig.CustomCollection.class))
			return true;

		if(isLeaf(field))
			return false;

		for(IFieldSpecs<ITypeExpansion> spec : expansions.get(field.getType()))
			if(spec.accept(field))
				return true;

		return false;
	}

	/**
	 * When there's no type expansion for this field,
	 *  gives a placeholder which assigns the instance on the id "". <p>
	 * Also if there's {@link DynamicConfig.IDSpecifier} annotation,
	 *  gives Adaptive one which automatically converts the id.
	 * */
	static ITypeExpansion getExpansion(DCfgFieldNode fieldNode) {
		// Check if this is custom collection.
		if(fieldNode.hasAnnotation(DynamicConfig.CustomCollection.class)) {
			Class<? extends ITypeExpansion> handler = fieldNode.getAnnotation(DynamicConfig.CustomCollection.class).customHandler();

			try {
				return handler.newInstance();
			} catch (Exception exception) {
				throw new IllegalArgumentException("The custom handler class should have constructor without any parameters", exception);
			}
		}

		// Query for the expansion.
		ITypeExpansion expansion = null;

		Field theField = fieldNode.getField();

		// Leaf node won't get any collection expansion
		if(isLeaf(theField))
			return expansion.SINGLETON;

		for(IFieldSpecs<ITypeExpansion> spec : expansions.get(fieldNode.getType())) {
			if(spec.accept(theField)) {
				expansion = spec.getObject(theField);
				break;
			}
		}

		// No expansion - Singleton case
		if(expansion == null)
			return expansion.SINGLETON;

		// Entries with specific Orderings
		if(fieldNode.hasAnnotation(DynamicConfig.Ordered.class)) {
			if(fieldNode.isOrderConfigurable())
				throw new IllegalStateException("Order-configurable collection can't have specific fixed order");

			Class<? extends DynamicConfig.IComparatorFactory> factory = fieldNode.getAnnotation(DynamicConfig.Ordered.class).comparatorFactory();

			try {
				return new ITypeExpansion.OrderedExpansion(expansion, factory.newInstance().createComparator());
			} catch (Exception exception) {
				throw new IllegalArgumentException("The factory class should have constructor without any parameters", exception);
			}
		}

		return expansion;
	}

	/** Check if this field is a leaf node. */
	static boolean isLeaf(Field field) {
		for(IFieldSpecs<IPropertyType> spec : leafProperties.get(field.getType()))
			if(spec.accept(field))
				return true;
		return false;
	}

	/** Check if this type can be a leaf node. */
	static boolean isLeaf(Class<?> valType) {
		for(IFieldSpecs<IPropertyType> spec : leafProperties.get(valType))
			if(spec.accept(null)) // This means it is leaf for any field.
				return true;
		return false;
	}

	static IPropertyType getPropertyType(Field field) {
		for(IFieldSpecs<IPropertyType> spec : leafProperties.get(field.getType()))
			if(spec.accept(field))
				return spec.getObject(field);
		return null;
	}

	static IPropertyType getPropertyType(Class<?> valType) {
		for(IFieldSpecs<IPropertyType> spec : leafProperties.get(valType))
			if(spec.accept(null)) // This means it is leaf for any field.
				return spec.getObject(null); // So get it.
		return null;
	}


	private static void applyDependence(Object instance, Object fieldValue, DCfgFieldNode fieldNode) {
		// TODO fill in these reflective operations.
		
	}

	private static Object restrictValue(Object value, Class<? extends Annotation> annType, Annotation annotation) {

		return null;
	}

	private static void restrictCollection(Object fieldValue, ITypeExpansion expansion, DCfgFieldNode fieldNode,
			Class<? extends Annotation> annType, Annotation annotation) {
		if(fieldNode.isConfigurable()) {
			// Restricts Nodes
			
		} else {
			// Restricts Instance
			
		}
	}

	private static Map.Entry<String, Object> createInstance(Object instance, Field field, Object fieldValue, String keyWanted, boolean isCollection) {
		if(isCollection) {
			
		} else throw new IllegalStateException("Non-collection field can't be configurable");
		return null;
	}

	private static void removeInstance(Object instance, Field field, Object fieldValue, String key, Object toRemove, boolean isCollection) {
		if(isCollection) {
			
		} else throw new IllegalStateException("Non-collection field can't be configurable");
	}


	static boolean isFieldAnnotation(Class<? extends Annotation> annotationType) {

		return false;
	}

	static boolean isElementAnnotation(Class<? extends Annotation> annotationType) {

		return false;
	}

	static boolean evaluateOnPhase(EnumConfigPhase phase, Class<? extends Annotation> annotationType) {

		return false;
	}

	// TODO Enums - how to handle those?
	private static class FixedListPropertySpec implements IFieldSpecs<IPropertyType> {
		private Class<?> elementType;
		private IPropertyType listPropType;

		private FixedListPropertySpec(Class<?> elementType) {
			this.elementType = elementType;
			// Search for the simple spec.
			for(IFieldSpecs<IPropertyType> elementPropType : leafProperties.get(elementType)) {
				if(elementPropType instanceof IFieldSpecs.SimpleSpecs) {
					this.listPropType = new IPropertyType.FixedListType(elementType,
							elementPropType.getObject(null));
					return;
				}
			}
		}

		@Override
		public boolean accept(Field field) {
			if(field == null)
				return false;

			if(field.isAnnotationPresent(DynamicConfig.Collection.class))
				if(field.getAnnotation(DynamicConfig.Collection.class).isConfigurable())
					return false;
			Type genType = field.getGenericType();
			if(genType instanceof ParameterizedType) {
				ParameterizedType parType = (ParameterizedType) genType;
				Type[] arguments = parType.getActualTypeArguments();
				if(arguments.length == 1)
					return arguments.equals(this.elementType);
			}

			return false;
		}

		@Override
		public IPropertyType getObject(Field field) {
			return this.listPropType;
		}
	}

	private static class MapExpansionSpec implements IFieldSpecs<ITypeExpansion> {

		@Override
		public boolean accept(Field field) {
			if(field == null)
				return false;

			Type type = field.getGenericType();
			if(type instanceof ParameterizedType) {
				ParameterizedType parType = (ParameterizedType) type;
				Type[] arguments = parType.getActualTypeArguments();
				if(arguments.length == 2)
					return arguments[0].equals(String.class);
			}

			return false;
		}

		@Override
		public ITypeExpansion getObject(Field field) {
			return ITypeExpansion.MAP;
		}
	}
}