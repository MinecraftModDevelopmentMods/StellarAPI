package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * Instance node which holds nodes storing the instance.
 * The instance is regarded as a value for leaf nodes.
 * */
public class DCfgInstanceNode implements IDCfgNode, IDCfgProperty {

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
	private IPropertyType propType;
	private String altType;

	final ImmutableSortedSet<Field> fieldsSet;
	final ImmutableMap<Field, DCfgFieldNode> fieldNodeMap;

	private final Map<Class<? extends Annotation>, Annotation> elementAnnotations = Maps.newHashMap();

	private boolean isChanged = false;
	private boolean syncNeeded = false;

	public DCfgInstanceNode(Class<?> superClass, Object value, IPropertyType propType) {
		this.type = superClass;
		this.fieldsSet = null;
		this.fieldNodeMap = null;
		this.propType = propType;
		this.isLeaf = true;
	}

	public DCfgInstanceNode(Class<?> superClass, Object instance) {
		this.instance = instance;
		this.isLeaf = false;

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

	boolean isSyncNeeded() {
		return this.syncNeeded;
	}

	void setSyncNeeded(boolean syncNeeded) {
		this.syncNeeded = syncNeeded;
	}

	@Override
	public boolean isLeafNode() {
		return this.isLeaf;
	}

	@Override
	public IDCfgProperty getProperty() {
		if(!this.isLeaf)
			throw new UnsupportedOperationException("Non-leaf node can't be a property");

		return this;
	}

	// IDCfgProperty starts here

	@Override
	public Object getValue() {
		return this.instance;
	}

	@Override
	public void setValue(Object value) {
		this.isChanged = true;
		setValueInternal(value);
	}

	@Override
	public boolean isValid(String strValue) {
		// TODO Restrictions
		return propType.isValid(strValue);
	}

	@Override
	public String toValidString(String incomplete) {
		// TODO Restrictions
		return propType.toValidString(incomplete);
	}

	@Override
	public String getValueAsString() {
		// TODO Restrictions
		return propType.toString(this.instance);
	}

	@Override
	public boolean setValue(String strValue) {
		// TODO Restrictions
		try {
			setValue(propType.fromString(strValue));
			return true;
		} catch(Exception exception) {
			return false;
		}
	}

	@Override
	public ITypeExpansion<?> alternative() {
		return propType.alternative(this.altType); // TODO alternative type - how to support switchables?
	}

	// IDCfgProperty ends here

	Object getInstance() {
		if(this.isLeaf)
			throw new UnsupportedOperationException("Leaf node can't have an instance");
		return this.instance;
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
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return (T) elementAnnotations.get(annotationType);
	}

	@Override
	public Class<? extends Annotation>[] getAnnotationTypes() {
		return elementAnnotations.keySet().toArray(new Class[0]);
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	// Instance node is not a field, thus it doesn't have any generic type.
	@Override
	public Type getGenericType() {
		return this.type;
	}

	@Override
	public boolean isCollection() {
		return false;
	}

	@Override
	public IDCfgCollection getCollection() {
		throw new UnsupportedOperationException("Can't get collection from a non-collection node.");
	}

	@Override
	public Iterable<Map.Entry<String, IDCfgNode>> getEntries() {
		if(this.isLeaf)
			return null;

		return new Iterable<Map.Entry<String, IDCfgNode>>() {
			@Override
			public Iterator<Map.Entry<String, IDCfgNode>> iterator() {
				return new EntryIterator();
			}
		};
	}

	@Override
	public Iterable<String> getKeys() {
		if(this.isLeaf)
			return null;

		return Iterables.transform(this.getEntries(),
				new Function<Map.Entry<String, IDCfgNode>, String>() {
					@Override
					public String apply(Map.Entry<String, IDCfgNode> input) {
						return input.getKey();
					}		
		});
	}

	@Override
	public Iterable<IDCfgNode> getChildNodes() {
		if(this.isLeaf)
			return null;

		return Iterables.transform(this.getEntries(),
				new Function<Map.Entry<String, IDCfgNode>, IDCfgNode>() {
					@Override
					public IDCfgNode apply(Map.Entry<String, IDCfgNode> input) {
						return input.getValue();
					}		
		});
	}

	private class EntryIterator implements Iterator<Map.Entry<String, IDCfgNode>> {
		private Stack<DCfgInstanceNode> parents = new Stack();
		private Stack<Iterator<Field>> parentIterators = new Stack();

		private DCfgInstanceNode currentIterated = DCfgInstanceNode.this;
		private Iterator<Field> iterator = fieldsSet.iterator();

		private DCfgFieldNode expandedColNode = null;
		private Iterator<String> iteratorCol = null;

		// remove/add unsupported - cache this!
		private Map.Entry<String, IDCfgNode> nextEntry = this.calculateNext(); 

		@Override
		public boolean hasNext() {
			return this.nextEntry != null;
		}

		@Override
		public Map.Entry<String, IDCfgNode> next() {
			Map.Entry<String, IDCfgNode> nextOne = this.nextEntry;
			this.nextEntry = this.calculateNext();
			return nextOne;
		}

		private Map.Entry<String, IDCfgNode> calculateNext() {
			if(this.expandedColNode != null) {
				if(iteratorCol.hasNext()) {
					// Collection element node can't be expanded.
					String key = iteratorCol.next();
					return Pair.<String, IDCfgNode>of(key, expandedColNode.getNode(key)); 
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
					return Pair.<String, IDCfgNode>of(field.getName(), fieldNode);
				} else return Pair.<String, IDCfgNode>of(field.getName(), fieldNode.getNode(""));
			}

			if(!parents.isEmpty()) {
				this.currentIterated = parents.pop();
				this.iterator = parentIterators.pop();
				return this.calculateNext();
			}

			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"This iterator does not support remove operation");
		}
	}
}