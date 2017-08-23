package stellarapi.api.lib.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * Describes expansion of a type. <p>
 * Note that only specified generic types(array/class) without variable arguments
 *  are accepted for configuration. <p>
 * 
 * gives illegal argument exception when the key is wrong.
 * */
public interface ITypeExpansion<C> {
	public boolean hasKey(C collection, String key);

	/** Get ordered keys. No need to implement remove. */
	public Iterable<String> getKeys(C collection);

	/**
	 * Gets the value for the valid key. Can throw exceptions for invalid keys.
	 * Can be null for leaf expansions.
	 * */
	public Object getValue(C collection, String key);
	public Class<?> getValueType(C collection, String key, Type colType);
	/**
	 * Sets value for the corresponding key.
	 * Returns third parameter on singletons, and returns the collection itself it it isn't.
	 * Can throw exceptions for invalid keys.
	 * 
	 * When putting a new instance, this will setValue it on the last location.
	 * */
	public Object setValue(C collection, String key, Object value);
	/** Removes the value for the valid key. Can throw exceptions for invalid keys. */
	public void remove(C collection, String key);

	/**
	 * Changes the key. When there's an preexisting element for the new key, exchanges the two.
	 * Can be omitted on purely order-dependent expansions like list.
	 * */
	public void changeKey(C collection, String oldKey, String newKey);

	/**
	 * Flag whether collection of this type is ordered, i.e. has modifiable order.
	 * */
	public boolean isOrdered();

	/**
	 * Generate an valid key from a suggested key.
	 * order is given to determine the order.
	 * */
	public String getValidKey(C collection, String suggestedKey, int order);

	/**
	 * Reorder this object based on the key. Should only be applied for collections.
	 * The components of the list should be exactly the same with the instance keys.
	 * If preOrder is empty, sort based on postOrder.
	 *  Else if preOrder and postOrder are same, only re-evaluates the key.
	 *  Otherwise, shuffle order with mapping preOrder -> postOrder.
	 * Also re-evaluates keys to apply changes in postOrder
	 *  and make it appropriate on order-dependent case.
	 * */
	public void reorder(C collection, List<String> preOrder, List<String> postOrder);

	/**
	 * Flag whether the key of this expansion is order-dependent or not.
	 * If an expansion is order-dependent, it should have a preserved order
	 *  which does not change on setValue/remove operation.
	 * Otherwise, the key should be invariable on the order.
	 * */
	public boolean isOrderDependent();

	/** Get the preserved order. */
	public Iterable<Map.Entry<String, Object>> preservedOrder(C collection);


	public static SingletonExpansion SINGLETON = new SingletonExpansion();

	public static class SingletonExpansion implements ITypeExpansion {
		private Set<String> keys = ImmutableSet.of("");
		private Set<String> empty = ImmutableSet.of();

		@Override
		public Iterable<String> getKeys(Object instance) {
			return instance != null? this.keys : this.empty;
		}

		@Override
		public Object getValue(Object instance, String key) {
			if(!key.isEmpty())
				throw new IllegalArgumentException(
						"Key expected to be empty, but got " + key + ".");
			return instance;
		}

		@Override
		public Class<?> getValueType(Object instance, String key, Type type) {
			return (Class<?>)type;
		}

		@Override
		public Object setValue(Object instance, String key, Object value) {
			if(!key.isEmpty())
				throw new IllegalArgumentException(
						"Key expected to be empty, but got " + key + ".");
			return value;
		}

		@Deprecated
		@Override
		public void remove(Object instance, String key) { }

		@Deprecated
		@Override
		public void changeKey(Object collection, String oldKey, String newKey) { }

		@Override
		public boolean hasKey(Object instance, String key) {
			return instance != null && key.isEmpty();
		}

		@Override
		public String getValidKey(Object instance, String suggestedKey, int newOrder) {
			return "";
		}

		@Override
		public boolean isOrdered() {
			return false;
		}

		@Override
		public void reorder(Object collection, List preOrder, List postOrder) {
			throw new UnsupportedOperationException(
					"Order-related operations are not allowed for a singleton.");
		}

		@Override
		public boolean isOrderDependent() {
			return false;
		}

		@Override
		public Iterable preservedOrder(Object collection) {
			return this.getKeys(collection);
		}
	}

	public static class OrderedExpansion implements ITypeExpansion {
		private ITypeExpansion parent;
		private Comparator<Pair<String, ?>> comparator;

		public OrderedExpansion(ITypeExpansion parent, Comparator<Pair<String, ?>> comparator) {
			this.parent = parent;
			this.comparator = comparator;
		}

		@Override
		public boolean hasKey(Object collection, String key) {
			return parent.hasKey(collection, key);
		}

		@Override
		public Iterable<String> getKeys(final Object collection) {
			final List<String> list = Lists.newArrayList(parent.getKeys(collection));
			Comparator<String> newComparator = Ordering.from(this.comparator).onResultOf(
					new Function<String, Pair<String, ?>>() {
						@Override
						public Pair<String, ?> apply(String input) {
							return Pair.of(input,
									parent.getValue(collection, input));
						}
					});
			Collections.sort(list, newComparator);
			return new Iterable() {
				@Override
				public Iterator iterator() {
					return list.iterator();
				}
			};
		}

		@Override
		public Object getValue(Object collection, String key) {
			return parent.getValue(collection, key);
		}

		@Override
		public Class<?> getValueType(Object instance, String key, Type type) {
			return parent.getValueType(instance, key, type);
		}

		@Override
		public Object setValue(Object collection, String key, Object value) {
			return parent.setValue(collection, key, value);
		}

		@Override
		public void remove(Object collection, String key) {
			parent.remove(collection, key);
		}

		@Override
		public void changeKey(Object collection, String oldKey, String newKey) {
			parent.changeKey(collection, oldKey, newKey);
		}

		@Override
		public String getValidKey(Object collection, String suggestedKey, int newOrder) {
			return parent.getValidKey(collection, suggestedKey, newOrder);
		}

		@Override
		public boolean isOrdered() {
			return true;
		}

		// Do nothing on reorder because instance order never changes.
		@Override
		public void reorder(Object collection, List preOrder, List postOrder) { }

		@Override
		public boolean isOrderDependent() {
			return parent.isOrderDependent();
		}

		@Override
		public Iterable preservedOrder(Object collection) {
			return parent.preservedOrder(collection);
		}
	}


	static final ListExpansion LIST = new ListExpansion();
	public static class ListExpansion implements ITypeExpansion<List> {
		@Override
		public boolean hasKey(List collection, String key) {
			try {
				int index = Integer.parseInt(key);
				return index >= 0 && index < collection.size();
			} catch(NumberFormatException exception) {
				return false;
			}
		}

		@Override
		public Iterable<String> getKeys(final List collection) {
			return new Iterable<String>() {
				@Override
				public Iterator<String> iterator() {
					return new Iterator<String>() {
						Iterator iterator = collection.iterator();
						int index = 0;

						@Override
						public boolean hasNext() { return iterator.hasNext(); }
						@Override
						public String next() {
							iterator.next();
							return String.valueOf(this.index++);
						}
						@Override
						public void remove() { iterator.remove(); }
					};
				}
			};
		}

		@Override
		public Object getValue(List collection, String key) {
			return collection.get(Integer.parseInt(key));
		}

		@Override
		public Class<?> getValueType(List collection, String key, Type type) {
			return (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0];
		}

		@Override
		public Object setValue(List collection, String key, Object value) {
			int num = Integer.parseInt(key);
			if(num == collection.size())
				collection.add(value);
			else collection.set(num, value);
			return collection;
		}

		@Override
		public void remove(List collection, String key) {
			collection.remove(Integer.parseInt(key));
		}

		@Override
		public String getValidKey(List collection, String suggestedKey, int newOrder) {
			return String.valueOf(newOrder);
		}

		@Override
		public void changeKey(List collection, String oldKey, String newKey) {
			// Is not going to work well, but... implement swap only. 
			int oldIndex = Integer.parseInt(oldKey);
			int newIndex = Integer.parseInt(newKey);
			if(0 <= oldIndex && oldIndex < collection.size() && 0 <= newIndex && newIndex < collection.size()) {
				Object oldElement = collection.get(oldIndex);
				collection.set(oldIndex, collection.get(newIndex));
				collection.set(newIndex, oldElement);
			}
		}

		@Override
		public boolean isOrdered() {
			return true;
		}

		@Override
		public void reorder(List collection, List<String> preOrder, List<String> postOrder) {
			if(!preOrder.equals(postOrder)) {
				if(preOrder.isEmpty()) {
					throw new IllegalStateException("Order-dependent collection should have its order tracked");
				} else {
					List col2 = Lists.newArrayList(collection.size());
					int index = 0;
					for(String key : postOrder)
						col2.set(index++, collection.get(preOrder.indexOf(key)));
					index = 0;
					for(Object object : col2)
						collection.set(index++, object);
				}
			}

			// No need to rearrange keys. It's directly dependent to the order.
		}

		@Override
		public boolean isOrderDependent() {
			return true;
		}

		@Override
		public Iterable<Map.Entry<String, Object>> preservedOrder(final List collection) {
			return new Iterable<Map.Entry<String, Object>> () {
				@Override
				public Iterator<Entry<String, Object>> iterator() {
					return new Iterator<Entry<String, Object>> () {
						Iterator<Object> iterator = collection.iterator();
						int index = 0;

						@Override
						public boolean hasNext() {
							return iterator.hasNext();
						}

						@Override
						public Map.Entry<String, Object> next() {
							return Pair.of(String.valueOf(this.index++), iterator.next());
						}
					};
				}
			};
		}
	}

	static final MapExpansion MAP = new MapExpansion();
	public static class MapExpansion implements ITypeExpansion<Map> {
		@Override
		public boolean hasKey(Map collection, String key) {
			return collection.containsKey(key);
		}

		@Override
		public Iterable<String> getKeys(Map collection) {
			return collection.keySet();
		}

		@Override
		public Object getValue(Map collection, String key) {
			return collection.get(key);
		}

		@Override
		public Class<?> getValueType(Map collection, String key, Type type) {
			return (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[1];
		}

		@Override
		public Object setValue(Map collection, String key, Object value) {
			collection.put(key, value);
			return collection;
		}

		@Override
		public void remove(Map collection, String key) {
			collection.remove(key);
		}

		@Override
		public String getValidKey(Map collection, String suggestedKey, int newOrder) {
			return suggestedKey;
		}

		@Override
		public void changeKey(Map collection, String oldKey, String newKey) {
			if(!collection.containsKey(oldKey))
				return;
			Object oldElement = collection.get(oldKey);
			if(collection.containsKey(newKey))
				collection.put(oldKey, collection.get(newKey));
			else collection.remove(oldKey);
			collection.put(newKey, oldElement);
		}

		@Override
		public boolean isOrdered() { return false; }

		@Override
		public void reorder(Map collection, List<String> preOrder, List<String> postOrder) { }

		@Override
		public boolean isOrderDependent() { return false; }

		@Override
		public Iterable<Entry<String, Object>> preservedOrder(Map collection) {
			return collection.entrySet(); // This is not 'preserved' order, but...
		}
	}
}