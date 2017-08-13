package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * Instance node which holds nodes storing the instance.
 * The instance is regarded as a value for leaf nodes.
 * */
public class DCfgInstanceNode implements IDCfgNode {

	private static final Function<Field, Integer> priority = new Function<Field, Integer>() {
		@Override
		public Integer apply(Field input) {
			if(!input.isAnnotationPresent(DynamicConfig.Priority.class))
				return 0;
			return input.getAnnotation(DynamicConfig.Priority.class).value();
		}
	};

	private Class<?> type;
	Object instance; // Only be able to be null for leaf nodes & root nodes.
	private boolean isLeaf;

	final ImmutableSortedSet<Field> fieldsSet;
	final ImmutableMap<Field, DCfgFieldNode> fieldNodeMap;

	private final Map<Class<? extends Annotation>, Annotation> elementAnnotations = Maps.newHashMap();

	private boolean isChanged = false;

	public DCfgInstanceNode(Class<?> superClass, Object instance, boolean isLeaf) {
		this.instance = instance;
		this.isLeaf = isLeaf;

		if(isLeaf) {
			this.type = superClass;
			this.fieldsSet = null;
			this.fieldNodeMap = null;
			return;
		}

		this.type = instance != null? instance.getClass() : superClass;
		ImmutableSortedSet.Builder<Field> setBuilder = ImmutableSortedSet.orderedBy(
				Ordering.<Integer>natural().reverse().onResultOf(priority));
		ImmutableMap.Builder<Field, DCfgFieldNode> mapBuilder = ImmutableMap.builder();

		for(Field field : type.getDeclaredFields()) {
			// Check for validity
			if (!Modifier.isPublic(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()) != (instance == null))
				continue;
			if (Modifier.isTransient(field.getModifiers()))
				continue;

			//Put information into the sets and the maps.
			setBuilder.add(field);

			DCfgFieldNode fieldNode = new DCfgFieldNode(field);
			mapBuilder.put(field, fieldNode);

			// Find field value
			Object fieldValue = DCfgManager.getFieldValue(field, instance);

			fieldNode.setupInitial(instance, fieldValue);
		}

		this.fieldsSet = setBuilder.build();
		this.fieldNodeMap = mapBuilder.build();
	}

	/** Puts annotation map. Removes null in this process */
	void putMapAnnotations(Map<Class<? extends Annotation>, Annotation> annotationsToPut) {
		elementAnnotations.putAll(annotationsToPut);

		Iterator<Annotation> ite = elementAnnotations.values().iterator();
		while(ite.hasNext())
			if(ite.next() == null)
				ite.remove();
	}

	@Override
	public boolean isLeafNode() {
		return this.isLeaf;
	}

	@Override
	public Object getValue() {
		if(!this.isLeaf)
			throw new UnsupportedOperationException("Non-leaf node can't have a value");
		return this.instance;
	}

	Object getInstance() {
		if(this.isLeaf)
			throw new UnsupportedOperationException("Leaf node can't have an instance");
		return this.instance;
	}

	@Override
	public void setValue(Object value) {
		this.isChanged = true;
		setValueInternal(value);
	}

	void setValueFromInstance(Object value) {
		setValueInternal(value);
		// TODO Notify to the theme handlers
	}

	private void setValueInternal(Object value) {
		if(!this.isLeaf)
			throw new IllegalStateException("Non-leaf node can't have a value");

		this.instance = value;
	}


	boolean isChanged() {
		if(this.isChanged)
			return true;
		for(DCfgFieldNode node : fieldNodeMap.values())
			if(node.isChanged())
				return true;
		return false;
	}

	void processChanged() {
		this.isChanged = false;
	}


	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return elementAnnotations.containsKey(annotationType);
	}

	@Override
	public Annotation getAnnotation(Class<? extends Annotation> annotationType) {
		return elementAnnotations.get(annotationType);
	}

	@Override
	public Class<? extends Annotation>[] getAnnotationTypes() {
		return elementAnnotations.keySet().toArray(new Class[0]);
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public boolean isCollection() {
		return false;
	}

	@Override
	public boolean isConfigurable() {
		throw new UnsupportedOperationException("Non-collection node can't be configurable");
	}

	@Override
	public boolean isOrderConfigurable() {
		throw new UnsupportedOperationException("Non-collection node can't be order-configurable");
	}

	@Override
	public ICfgIterator<IDCfgNode> getChildNodeIte() {
		if(this.isLeaf)
			return null;
		return new ICfgIterator<IDCfgNode>() {
			private Stack<DCfgInstanceNode> parents = new Stack();
			private Stack<Iterator<Field>> parentIterators = new Stack();

			private DCfgInstanceNode currentIterated = DCfgInstanceNode.this;
			private Iterator<Field> iterator = fieldsSet.iterator();

			private DCfgFieldNode expandedColNode = null;
			private Iterator<String> iteratorCol = null;

			// remove/add unsupported - cache this!
			private IDCfgNode nextNode = this.calculateNext(); 

			@Override
			public boolean hasNext() {
				return this.nextNode != null;
			}

			@Override
			public IDCfgNode next() {
				IDCfgNode nextOne = this.nextNode;
				this.nextNode = this.calculateNext();
				return nextOne;
			}
			
			private IDCfgNode calculateNext() {
				if(this.expandedColNode != null) {
					if(iteratorCol.hasNext()) {
						// Collection element node can't be expanded.
						String key = iteratorCol.next();
						return expandedColNode.getNode(key); 
					} else {
						expandedColNode = null;
						iteratorCol = null;
					}
				}

				while(iterator.hasNext()) {
					Field field = iterator.next();
					DCfgFieldNode fieldNode = currentIterated.fieldNodeMap.get(field);
					if(fieldNode.keys().isEmpty())
						continue;

					if(fieldNode.hasAnnotation(DynamicConfig.Expand.class)) {
						if(fieldNode.isCollection()) {
							this.expandedColNode = fieldNode;
							this.iteratorCol = fieldNode.sortedKeys().iterator();
							return this.calculateNext();
						} else {
							parents.push(this.currentIterated);
							parentIterators.push(this.iterator);

							this.currentIterated = fieldNode.getNode("");
							this.iterator = currentIterated.fieldsSet.iterator();
							continue;
						}
					} else if(fieldNode.isCollection()) {
						return fieldNode;
					} else return fieldNode.getNode("");
				}

				if(!parents.isEmpty()) {
					this.currentIterated = parents.pop();
					this.iterator = parentIterators.pop();
					return this.calculateNext();
				}

				return null;
			}

			@Override
			public void add(String key) {
				throw new UnsupportedOperationException(this.expandedColNode == null?
						"Non-collection nodes does not support add operation" :
							"Immutable collection can't be iterated.");
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"Non-collection nodes does not support remove operation");
			}
		};
	}
}