package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marking Dynamic Configuration.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicConfig {

	/**
	 * The user-friendly name for the configuration file.
	 * */
	String name() default "";

	/**
	 * Declares that annotation properties for this field are determined dynamically.<p>
	 * The evaluator method should be static and accessible, and has one parameter - the instance.
	 *  Also, it should return the annotation to apply -
	 *  return <code>null</code> to nullify the annotation. <p>
	 * Leaving handler as Object.class(default) means the parent class evaluates the value.
	 *  In this case, the method should have the same <code>static</code flag with the field 
	 *  and it shouldn't have any parameter.<p>
	 * Can be applied to any field, while {@link DynamicFieldProperty} can't specify this one.<p>
	 * Usually, evaluated once when the relevant node is created.
	 *  For some annotations it's evaluated just before applying restrictions.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DynamicFieldProperty {
		/** List of affected annotations. */
		Class<? extends Annotation>[] affected() default {};
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * Similar with {@link DynamicFieldProperty}, except that this is applied to each elements. <p>
	 * The evaluator method should have two more parameters than its field counterpart,
	 *  which are the key and the element to evaluate the annotation for.<p>
	 * Since on collections it's impossible to assign properties to each of the elements,
	 *  this one allows them to have generated properties(annotations).<p>
	 * Usually, evaluated once when the relevant node is created
	 *  with the exception of restrictions which are evaluated on restriction time.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DynamicElementProperty {
		Class<? extends Annotation>[] affected() default {};
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * Defines ID specifier for collection fields,
	 *  to provide reliable reordering for lists. <p>
	 * The ID will be evaluated on any time, so it should be constant over time. <p>
	 * This won't work to change a key for each field. <p>
	 * <p><br>
	 * Warning: Any operation around this can be slow.
	 * */
	/*@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface IDSpecifier {
		Class<?> handler() default Object.class;
		String id() default "";
	}*/


	/**
	 * Priority of this field. <p>
	 * Fields with higher priority will be loaded/saved earlier.
	 *  (Priority of a field is 0 by default, even w/o this annotation) <p>
	 * Among fields of the same priority,
	 *  fields will be loaded/saved in order of declaration
	 *  with exception of changed fields being at first and added fields at last. <p>
	 * Fields with higher priority will get higher chance to be overwritten. <p>
	 * 
	 * Can be applied to any field, while {@link DynamicFieldProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Priority {
		int value() default 0;
	}

	/**
	 * Marks a field as collection, with remove/add handling.<p>
	 * Leaving handler as Object.class(default) means the value is set by default. <p>
	 * <br>
	 * About a collection: Instance of a collection <b>must</b> not be changed.
	 *  It shouldn't be <code>null</code> as well. <br>
	 * Also note that a collection in collection is not allowed
	 *  - use a dummy class for that.<p>
	 * <br>
	 * If a node of a collection is a property,
	 *  all the other nodes should be a property as well.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Collection {
		/** 
		 * If true, objects can be added/removed from this field according to the configuration.
		 * Do NOT change the reference of the element assigned to a key in a configurable collection.
		 * Do NOT add/remove an element from the instance side in a configurable collection.
		 * */
		boolean isConfigurable() default false;

		/**
		 * If true, order of objects is determined by the configuration
		 *  and injected into the configurable nodes.
		 * Otherwise, order of instance is applied to the configuration.
		 * */
		boolean isConfigurableOrder() default false;

		/** The handler of this collection field.  */
		Class<?> handler() default Object.class;
		/** The ID of this collection field. Needed for configurable collection */
		String id() default "";
	}

	/**
	 * Custom collection handling.
	 * Type expansion.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface CustomCollection {
		Class<? extends ITypeExpansion> customHandler();
	}

	/**
	 * Expands a field, i.e. the categories and properties from the field
	 *  is merged to the parent category. <p>
	 * No other annotations are allowed for the field, otherwise .<p>
	 * Assigning a collection here will expands the elements on the parent category.
	 * The collection should be immutable, or IllegalStateException will be thrown.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Expand { }


	/**
	 * Marks a <b>field</b> as a dependent variable on the configuration. <p>
	 * Without this, changing value from/to null is impossible in this system.
	 *  (It is prohibited to set collections to null / change type of the collection)<p>
	 * Be careful of referencing properties which has this annotation as well -
	 * It could be entangled if the referenced properties aren't loaded first. <p>
	 * However, stabilizing cross-reference is fine. <p>
	 * Also don't reference node which could be nonexistent,
	 *  i.e. field of A can reference field of B only if
	 *   B exists whenever A exists without single change of instance. <p>
	 * <br>
	 * This annotation can't be dynamically evaluated.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Dependence {
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * Sets a field as a representative for the name of the instance.<p>
	 * Can't be dynamic. It's better to have this one as transient.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface NameField { }

	/**
	 * Declares the dynamic default evaluator for the field. <p>
	 * This is to provide the case-dependent Default.
	 *  (Lack of this annotation means the default is the initial value of the field) <p>
	 * <br>
	 * Leaving handler as Object.class(default) means the value is set by default. <p>
	 * <br>
	 * Can only be applied to the leaf field, and {@link DynamicFieldProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Default {
		Class<?> handler() default Object.class;
		String id() default "";
	}


	/**
	 * Cycles boolean. Won't be applied for file-type configurations.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface BooleanCycle {
		/**
		 * What each of false and true actually represents.
		 * */
		String[] value() default {"false", "true"};
	}

	/**
	 * Cycles String. Works as restriction on String as well.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface StringCycle {
		String[] value();
	}


	/**
	 * Specifies the preset for ambiguous non-native leaf-properties.
	 * e.g. Vector3 could be expressed as either (x,y,z) or (r,t,p).
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Preset {
		String value();
	}



	/**
	 * Specifies the evaluator id for methods.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface EvaluatorID {
		String value();
	}
}