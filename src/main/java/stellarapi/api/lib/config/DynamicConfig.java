package stellarapi.api.lib.config;

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
	 * <br>
	 * Leaving handler as Object.class(default) means the parent class evaluates the value.<p>
	 * <br>
	 * Can be applied to any field, while {@link DynamicProperty} can't specify this one.<p>
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DynamicProperty {
		Class<?>[] affected() default {};
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * Declares the dynamic default evaluator for the field. <p>
	 * This is to provide the case-dependent Default.
	 *  (Lack of this annotation means the default is the initial value of the field) <p>
	 * <br>
	 * Leaving handler as Object.class(default) means the value is set by default. <p>
	 * <br>
	 * Can only be applied to the leaf field, and {@link DynamicProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Default {
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * Priority of this field. <p>
	 * Fields with higher priority will be loaded/saved earlier.
	 *  (Priority of a field is 0 by default, even w/o this annotation) <p>
	 * Among fields of the same priority,
	 *  fields will be loaded/saved in order of declaration
	 *  with exception of changed fields being at first and added fields at last. <p>
	 * Fields with higher priority will get higher chance to be overwritten. <p>
	 * 
	 * Can be applied to any field, while {@link DynamicProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Priority {
		int value() default 0;
	}

	/**
	 * Marks a field as collection, with remove/add handling.<p>
	 * <br>
	 * Note that a collection in collection is not allowed
	 *  - use a dummy class for that.<p>
	 * Also, if a node of a collection is a property,
	 *  all the other nodes should be a property as well.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Collection {
		/** If true, objects can be added/removed from this field according to the configuration. */
		boolean isConfigurable() default false;

		/** The ID of this collection field. Needed for configurable collection */
		String id() default "";
	}

	/**
	 * Expands a field, i.e. the categories and properties from the field
	 *  is merged to the parent category. <p>
	 * No other annotations are allowed for the field. Can't assign this on a collection field.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Expand { }


	/**
	 * Marks a field as a dependent variable on the configuration. <p>
	 * Don't reference properties from the child configs, it can cause problems.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Dependent {
		/** The ID of the dependence. */
		String value() default "";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface BooleanCycle {
		/**
		 * What each of false and true actually represents.
		 * */
		String[] value() default {"false", "true"};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface StringCycle {
		String[] value();
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