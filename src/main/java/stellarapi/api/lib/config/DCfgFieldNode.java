package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * Node on each field of the configuration class.
 * 
 * */
public class DCfgFieldNode implements IDCfgNode {
	private Field field;
	private Class<?> fieldType; // Type of this field. Useful for collections & leaves.
	private boolean isCollection, isConfigurable, isOrderConfigurable;
	private boolean isLeafParent;

	private final List<String> order = Lists.newArrayList();
	private final Map<String, DCfgInstanceNode> childNodes = Maps.newHashMap(); // Needs SingletonMap impl.

	// The result order this field node collection is going to have.
	private final List<String> postOrder = Lists.newArrayList();

	private final Set<String> toAdd = Sets.newHashSet();
	private final Set<String> toRemove = Sets.newHashSet();

	private Map<Class<? extends Annotation>, Annotation> fieldAnnotations = Maps.newHashMap();
	private ImmutableMap<Class<? extends Annotation>, Annotation> thoseForElements;

	private boolean isChanged = false;

	DCfgFieldNode(Field field) {
		this.field = field;
		this.isCollection = DCfgManager.isCollection(field);
		this.isLeafParent = DCfgManager.isLeaf(field);
		this.fieldType = field.getType();

		if(this.isCollection) {
			DynamicConfig.Collection colInfo = field.getAnnotation(DynamicConfig.Collection.class);
			this.isConfigurable = colInfo != null && colInfo.isConfigurable();
			this.isOrderConfigurable = colInfo != null && colInfo.isConfigurableOrder();

			if(this.isConfigurable && field.isAnnotationPresent(DynamicConfig.Expand.class))
				throw new IllegalStateException(
						"@Expand can't be on configurable collection, check field " + field);
		}
	}

	// Only called once after some instance-side initialization.
	void setupInitial(Object instance, Object fieldValue) {
		this.storeFixedAnnotations();
		this.updateFieldAnnotations(instance, EnumConfigPhase.Creation);

		ITypeExpansion expansion = DCfgManager.getExpansion(this);
		for(String key : expansion.getKeys(fieldValue)) {
			Object subValue = expansion.getValue(fieldValue, key);
			this.createNode(instance, key, subValue);
		}

		postOrder.addAll(this.order);
	}

	private void storeFixedAnnotations() {
		ImmutableMap.Builder<Class<? extends Annotation>, Annotation> builder = ImmutableMap.builder();

		for(Annotation annotation : field.getAnnotations()) {
			if(DCfgManager.isElementAnnotation(annotation.annotationType()))
				builder.put(annotation.annotationType(), annotation);
			else fieldAnnotations.put(annotation.annotationType(), annotation);
		}

		this.thoseForElements = builder.build();
	}

	void updateAnnotations(Object instance, EnumConfigPhase phase) {
		updateFieldAnnotations(instance, phase);

		for(Map.Entry<String, DCfgInstanceNode> childPair : childNodes.entrySet())
			this.updateElementAnnotations(instance, childPair.getKey(), childPair.getValue(),
					childPair.getValue().instance, phase);
	}

	/**Update dynamic field annotations on specific config phase*/
	private void updateFieldAnnotations(Object instance, EnumConfigPhase phase) {
		if(!field.isAnnotationPresent(DynamicConfig.DynamicFieldProperty.class))
			return;

		ImmutableMap.Builder<Class<? extends Annotation>, Annotation> builder = ImmutableMap.builder();

		DynamicConfig.DynamicFieldProperty dynamicEv = field.getAnnotation(DynamicConfig.DynamicFieldProperty.class);
		Class<?> handlerClass = dynamicEv.handler();
		Class<?> declaringClass = field.getDeclaringClass();
		if(handlerClass == Object.class)
			handlerClass = declaringClass;

		Set<Class<? extends Annotation>> annSet =
				ImmutableSet.copyOf(dynamicEv.affected());

		Class<?>[] paramTypes = handlerClass != declaringClass? new Class<?>[] { declaringClass } : new Class<?>[0];

		for(Method method : handlerClass.getDeclaredMethods()) {
			if(!method.isAnnotationPresent(DynamicConfig.EvaluatorID.class))
				continue;
			if(!method.getAnnotation(DynamicConfig.EvaluatorID.class).value().equals(dynamicEv.id()))
				continue;

			//Checks if this method can be called.
			{
				int i = 0;
				Class<?>[] methodParamTypes = method.getParameterTypes();
				for(i = 0; i < paramTypes.length; i++)
					if(!methodParamTypes[i].isAssignableFrom(paramTypes[i]))
						break;
				if(i != paramTypes.length)
					continue;
			}

			if(!Annotation.class.isAssignableFrom(method.getReturnType()))
				continue;

			Class<? extends Annotation> annType = (Class<? extends Annotation>) method.getReturnType();

			if(!annSet.contains(annType))
				continue;

			if(!method.isAccessible())
				Throwables.propagate(new IllegalAccessException("Can't access evaluator method: " + method));
			if((handlerClass != declaringClass || instance == null) != Modifier.isStatic(method.getModifiers()))
				Throwables.propagate(new IllegalAccessException("Static flag inverted on evaluator method: " + method));

			if(DCfgManager.evaluateOnPhase(phase, annType)) {
				try {
					Annotation annotation;

					if(handlerClass != declaringClass)
						annotation = (Annotation) method.invoke(null, instance);
					else annotation = (Annotation) method.invoke(instance);

					builder.put(annType, annotation);
				} catch (Exception exception) {
					Throwables.propagate(exception); //Can't reach here
				}
			}
		}

		this.putMapAnnotations(builder.build());
	}

	/**Update dynamic eleement annotations for elements on specific config phase*/
	private void updateElementAnnotations(Object instance, String key, DCfgInstanceNode childNode, Object childValue, EnumConfigPhase phase) {
		if(!field.isAnnotationPresent(DynamicConfig.DynamicElementProperty.class))
			return;

		ImmutableMap.Builder<Class<? extends Annotation>, Annotation> builder = ImmutableMap.builder();

		DynamicConfig.DynamicElementProperty dynamicEv = field.getAnnotation(DynamicConfig.DynamicElementProperty.class);
		Class<?> handlerClass = dynamicEv.handler();
		Class<?> declaringClass = field.getDeclaringClass();
		if(handlerClass == Object.class)
			handlerClass = declaringClass;

		Set<Class<? extends Annotation>> annSet =
				ImmutableSet.copyOf(dynamicEv.affected());

		Class<?>[] paramTypes = handlerClass != declaringClass?
				new Class<?>[] {declaringClass, String.class, childNode.getType()} :
					new Class<?>[] {String.class, childNode.getType()};

		for(Method method : handlerClass.getDeclaredMethods()) {
			if(!method.isAnnotationPresent(DynamicConfig.EvaluatorID.class))
				continue;
			if(!method.getAnnotation(DynamicConfig.EvaluatorID.class).value().equals(dynamicEv.id()))
				continue;

			if(paramTypes.length != method.getParameterCount())
				continue;


			//Checks if this method can be called.
			{
				int i = 0;
				Class<?>[] methodParamTypes = method.getParameterTypes();
				for(i = 0; i < paramTypes.length; i++)
					if(!methodParamTypes[i].isAssignableFrom(paramTypes[i]))
						break;
				if(i != paramTypes.length)
					continue;
			}

			if(!Annotation.class.isAssignableFrom(method.getReturnType()))
				continue;

			Class<? extends Annotation> annType = (Class<? extends Annotation>) method.getReturnType();

			if(!annSet.contains(annType))
				continue;

			if(!method.isAccessible())
				Throwables.propagate(new IllegalAccessException("Can't access evaluator method: " + method));
			if((handlerClass != declaringClass || instance == null) != Modifier.isStatic(method.getModifiers()))
				Throwables.propagate(new IllegalAccessException("Static flag inverted on evaluator method: " + method));

			if(DCfgManager.evaluateOnPhase(phase, annType)) {
				try {
					Annotation annotation;

					if(handlerClass != declaringClass)
						annotation = (Annotation) method.invoke(null, instance);
					else annotation = (Annotation) method.invoke(instance);

					builder.put(annType, annotation);
				} catch (Exception exception) {
					Throwables.propagate(exception); //Can't reach here
				}
			}
		}

		this.putMapAnnotations(builder.build());
	}

	/** Puts annotation map. Removes null in this process */
	void putMapAnnotations(Map<Class<? extends Annotation>, Annotation> annotationsToPut) {
		fieldAnnotations.putAll(annotationsToPut);

		Iterator<Annotation> ite = fieldAnnotations.values().iterator();
		while(ite.hasNext())
			if(ite.next() == null)
				ite.remove();
	}


	@Override
	public boolean isCollection() {
		return this.isCollection;
	}

	@Override
	public boolean isConfigurable() {
		if(!this.isCollection)
			return false;
		return this.isConfigurable;
	}

	public boolean isOrderConfigurable() {
		if(!this.isCollection)
			throw new UnsupportedOperationException("Only collection node can be order-configurable or not.");
		return this.isOrderConfigurable;
	}

	public boolean isLeafParent() {
		return this.isLeafParent;
	}


	Set<String> keys() {
		return childNodes.keySet();
	}

	List<String> sortedKeys() {
		return this.order;
	}


	Set<String> requestedToAdd() {
		return this.toAdd;
	}

	Set<String> requestedToRemove() {
		return this.toRemove;
	}


	boolean hasKey(String key) {
		return childNodes.containsKey(key);
	}

	void createNode(Object instance, String key, Object childValue) {
		DCfgInstanceNode childNode = new DCfgInstanceNode(this.fieldType, childValue, this.isLeafParent);
		childNodes.put(key, childNode);
		order.add(key); // Add to the order.

		childNode.putMapAnnotations(this.thoseForElements);
		this.updateElementAnnotations(instance, key, childNode, childValue, EnumConfigPhase.Creation);

		// TODO Notify to the theme handlers
	}

	DCfgInstanceNode addRequested(Object instance, String reqKey, String key, Object childValue) {
		DCfgInstanceNode childNode = new DCfgInstanceNode(this.fieldType, childValue, this.isLeafParent);
		childNodes.put(key, childNode);

		// Replace the key
		Collections.replaceAll(this.postOrder, reqKey, key);

		childNode.putMapAnnotations(this.thoseForElements);
		this.updateElementAnnotations(instance, key, childNode, childValue, EnumConfigPhase.Creation);

		// TODO Notify to the theme handlers

		return childNode;
	}

	void removeRequested(String key) {
		// TODO Notify to the theme handlers

		childNodes.remove(key);
	}

	void onRemove(String key) {
		// TODO Notify to the theme handlers

		order.remove(key);
	}

	void refreshRequest() {
		order.clear();
		order.addAll(this.postOrder);

		toAdd.clear();
		toRemove.clear();
	}

	// Updates post order to the normal order, when the change is not applied.
	void updatePostOrder() {
		postOrder.clear();
		postOrder.addAll(this.order);
	}


	// Only called on DCfgManager sync methods
	void reorder(List<String> order) {
		try {
			Collections.sort(this.order, Ordering.<String>explicit(order));
			postOrder.clear();
			postOrder.addAll(this.order);
		} catch(ClassCastException exception) {
			throw new IllegalArgumentException(
					"the order should cover whole array. The instance and the node might not be synced");
		}
	}


	DCfgInstanceNode getNode(String key) {
		return childNodes.get(key);
	}


	public boolean isChanged() {
		if(this.isChanged)
			return true;
		for(DCfgInstanceNode node : childNodes.values())
			if(node.isChanged())
				return true;
		return false;
	}

	void processChanged() {
		this.isChanged = false;
	}


	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return fieldAnnotations.containsKey(annotationType);
	}

	@Override
	public Annotation getAnnotation(Class<? extends Annotation> annotationType) {
		return fieldAnnotations.get(annotationType);
	}

	@Override
	public Class<? extends Annotation>[] getAnnotationTypes() {
		return fieldAnnotations.keySet().toArray(new Class[0]);
	}

	@Override
	public Class<?> getType() {
		if(!this.isCollection)
			throw new UnsupportedOperationException("Non-collection field node shouldn't be exposed.");
		return this.fieldType;
	}

	@Override
	public boolean isLeafNode() {
		return false;
	}

	@Override
	public Object getValue() {
		throw new UnsupportedOperationException("A field node can't exchange values.");
	}

	@Override
	public void setValue(Object value) {
		throw new UnsupportedOperationException("A field node can't exchange values.");
	}


	/**
	 * Gets children iterator. This is given to
	 *  1. External user by interface IDCfgNode
	 *  2. Configurable collection restrictions
	 * */
	@Override
	public ICfgIterator<IDCfgNode> getChildNodeIte() {
		return new ICfgIterator<IDCfgNode>() {
			private DCfgFieldNode parent = DCfgFieldNode.this;
			private ListIterator<String> iterator = postOrder.listIterator();
			private String lastKey;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public IDCfgNode next() {
				return childNodes.get(this.lastKey = iterator.next());
			}

			@Override
			public void add(String key) {
				if(!isConfigurable)
					throw new UnsupportedOperationException(
							"Can't request node addition to non-configurable field.");

				toAdd.add(key);
				iterator.add(key);
				isChanged = true;
			}

			@Override
			public void remove() {
				if(!isConfigurable)
					throw new UnsupportedOperationException("Can't request node addition to non-configurable field.");

				toRemove.add(this.lastKey);
				iterator.remove();
				isChanged = true;
			}
		};
	}
}