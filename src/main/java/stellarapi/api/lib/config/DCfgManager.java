package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

/**
 * Configuration manager.
 */
public final class DCfgManager {

	public static final String SEPARATOR = ".";

	/**
	 * Read the config-nodes into the instance configuration.
	 * */
	private static void inputConfig(Class<?> cls, Object instance, DCfgInstanceNode node) {
		for(Field field : node.fieldsSet) {
			Object fieldValue = getFieldValue(field, instance);
			DCfgFieldNode fieldNode = node.fieldNodeMap.get(field);

			if((fieldValue == null && !fieldNode.isLeafParent()) || !fieldNode.isChanged())
				continue;

			// Sets config node value and propagates this one.
			ITypeExpansion expansion = DCfgManager.getExpansion(fieldNode);

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

			if(fieldValue == null && !fieldNode.isLeafParent())
				continue;

			ITypeExpansion expansion = DCfgManager.getExpansion(fieldNode);

			// Applies restrictions. Only leaf nodes can get these restrictions.
			for(String key : fieldNode.keys()) {
				if(!expansion.hasKey(instance, key))
					continue;

				DCfgInstanceNode childNode = fieldNode.getNode(key);
				if(childNode.isLeafNode())
					for(Class<? extends Annotation> annType : childNode.getAnnotationTypes()) {
						Object newValue = restrictValue(expansion.getValue(fieldValue, key), annType, childNode.getAnnotation(annType));
						expansion.setValue(fieldValue, key, newValue);
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
			ITypeExpansion expansion = DCfgManager.getExpansion(fieldNode);

			// Synchronizes the collections - direction depends on the mutability(configurability)
			//  If the collection is configurable, requested changes are going to be applied.
			// Also propagation is done here.
			if(fieldNode.isConfigurable() && fieldNode.isChanged()) {
				// Changed flag is for initial conditions where the nodes are prepared.
				// Especially when the collection is computed late with the dependency.
				// Here, nothing actually changed so instance and node should match.

				for(String key : fieldNode.keys()) {
					if(fieldNode.requestedToRemove().contains(key))
						continue;

					DCfgInstanceNode childNode = fieldNode.getNode(key);

					if(!childNode.isLeafNode()) {
						Object childInstance = expansion.getValue(fieldValue, key);
						finishNoutputConfig(childInstance.getClass(), childInstance, childNode);
					}
				}

				for(String keyToAdd : fieldNode.requestedToAdd()) { // Nodes are added here
					Object childInstance = createInstance(instance, field, fieldValue, keyToAdd, fieldNode.isCollection());
					String key = expansion.adaptiveKey(fieldValue, keyToAdd);
					DCfgInstanceNode childNode = fieldNode.addRequested(instance, keyToAdd, key, childInstance);
					expansion.setValue(fieldValue, key, childInstance);
					if(!childNode.isLeafNode())
						sync(childInstance.getClass(), childInstance, childNode);
				}

				for(String keyToRemove : fieldNode.requestedToRemove()) {
					Object childInstance = expansion.getValue(fieldValue, keyToRemove);
					fieldNode.removeRequested(keyToRemove);
					removeInstance(instance, field, fieldValue, keyToRemove, childInstance, fieldNode.isCollection());
				}

				// Refreshes the request and establishes the (new) order.
				fieldNode.refreshRequest();
			} else {
				for(String key : expansion.getKeys(fieldValue)) {
					Object childValue = expansion.getValue(fieldValue, key);

					if(!fieldNode.hasKey(key)) { // Added instance
						fieldNode.createNode(instance, key, childValue);
						DCfgInstanceNode childNode = fieldNode.getNode(key);
						if(!childNode.isLeafNode())
							sync(childValue.getClass(), childValue, childNode);
					} else if(!fieldNode.isLeafParent()) {
						DCfgInstanceNode childNode = fieldNode.getNode(key);
						if(childValue != childNode.getInstance()) { // Reference changes
							fieldNode.onRemove(key);
							fieldNode.createNode(instance, key, childValue);
							sync(childValue.getClass(), childValue, fieldNode.getNode(key));
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
			if(fieldNode.isCollection()) {
				if(fieldNode.isOrderConfigurable() && fieldNode.isChanged()) {
					expansion.reorder(fieldValue, fieldNode.sortedKeys());
				} else {
					fieldNode.reorder(ImmutableList.copyOf(expansion.getKeys(fieldValue)));
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
		return false;
	}

	/**
	 * When there's no type expansion for this field,
	 *  gives a placeholder which assigns the instance on the id "". <p>
	 * Also if there's {@link DynamicConfig.IDSpecifier} annotation,
	 *  gives Adaptive one which automatically converts the id.
	 * */
	static ITypeExpansion getExpansion(DCfgFieldNode fieldNode) {
		if(fieldNode.hasAnnotation(DynamicConfig.CustomCollection.class)) {
			
		}

		//if()

		return null;
	}

	static boolean isLeaf(Field field) {
		return false;
	}


	private static void applyDependence(Object instance, Object fieldValue, DCfgFieldNode fieldNode) {

		
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

	private static Object createInstance(Object instance, Field field, Object fieldValue, String keyWanted, boolean isCollection) {
		if(isCollection) {
			
		} else throw new IllegalStateException("Non-collection field can't be configurable");
		return null;
	}

	private static void removeInstance(Object instance, Field field, Object fieldValue, String key, Object toRemove, boolean isCollection) {
		if(isCollection) {
			
		} else throw new IllegalStateException("Non-collection field can't be configurable");
	}


	static boolean isElementAnnotation(Class<? extends Annotation> annotationType) {
		
		return false;
	}

	static boolean evaluateOnPhase(EnumConfigPhase creation, Class<? extends Annotation> annotationType) {

		return false;
	}
}
