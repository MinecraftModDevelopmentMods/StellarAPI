package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;

/**
 * Abstraction of dynamic configuration node for handler-side.
 * */
public interface IDCfgNode {
	public boolean hasAnnotation(Class<? extends Annotation> annotationType);
	public Annotation getAnnotation(Class<? extends Annotation> annotationType);
	public Class<? extends Annotation>[] getAnnotationTypes();

	public Class<?> getType();

	/** If this is a leaf node. */
	public boolean isLeafNode();
	/** If this is a collection node. */
	public boolean isCollection();

	/** Gets the value. Only for leaves. */
	public Object getValue();
	/** Gets child nodes. Two iterators in a row is not allowed. */
	public ICfgIterator<IDCfgNode> getChildNodeIte();

	/** Sets the value. */
	public void setValue(Object value);

	/** Checks if this property is configurable. always false for non-collection nodes. */
	public boolean isConfigurable();
	public boolean isOrderConfigurable();
}
