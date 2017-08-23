package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Abstraction of dynamic configuration node for handler-side.
 * */
public interface IDCfgNode {
	public boolean hasAnnotation(Class<? extends Annotation> annotationType);
	public <T extends Annotation> T getAnnotation(Class<T> annotationType);
	public Class<? extends Annotation>[] getAnnotationTypes();

	/** Gets the type. */
	public Class<?> getType();

	/** Gets the generic type. */
	public Type getGenericType();

	/** If this is a leaf node. */
	public boolean isLeafNode();
	/** Gets the assigned property for this node. Only applicable for leaf nodes. */
	public IDCfgProperty getProperty();

	/** If this is a collection node. */
	public boolean isCollection();
	/** Gets the collection for this node. Only applicable for collection nodes. */
	public IDCfgCollection getCollection();


	/** Get entries for the nodes. Remove operation is prohibited to use here. */
	public Iterable<Map.Entry<String, IDCfgNode>> getEntries();

	/** Gets keys for the nodes. Remove operation is prohibited to use here. */
	public Iterable<String> getKeys();

	/** Gets child nodes. Remove operation is prohibited to use here. */
	public Iterable<IDCfgNode> getChildNodes();
}
