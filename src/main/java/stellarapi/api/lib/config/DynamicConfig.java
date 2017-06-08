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
	 * Leaving target as Object.class(default) means it's evaluated for the final descendent.<br>
	 * Leaving handler as Object.class(default) means the parent class evaluates the value.<p>
	 * <br>
	 * Can be applied to any field, while {@link DynamicProperty} can't specify this one.<p>
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DynamicProperty {
		Class<?>[] affected() default {};
		Class<?> target() default Object.class;
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * To express Multiple Dynamic Properties.
	 * Mutually exclusive with {@link DynamicProperty}.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface DynamicProperties {
		DynamicProperty[] value() default {};
	}

	/**
	 * Declares the dynamic default evaluator for the field. <p>
	 * This is to provide the immutable Default.
	 *  (Lack of this annotation means the default is the initial value of the field) <p>
	 * <br>
	 * Leaving target as Object.class(default) means it's evaluated for the final descendent. <br>
	 * Leaving handler as Object.class(default) means the value is . <p>
	 * <br>
	 * Can be applied to any field, while {@link DynamicProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Default {
		Class<?> target() default Object.class;
		Class<?> handler() default Object.class;
		String id() default "";
	}

	/**
	 * To express Multiple Dynamic Defaults. <p>
	 * Mutually exclusive with {@link Default}.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Defaults {
		Default[] value() default {};
	}

	/**
	 * Priority of this field. <p>
	 * Fields with higher priority will be loaded/saved earlier.
	 *  (Priority of a field is 0 by default, even w/o this annotation) <p>
	 * Among fields of the same priority,
	 *  fields will be loaded/saved in order of declaration
	 *  with exception of changed fields being at first and added fields at last. <p>
	 *  
	 * Can be applied to any field, while {@link DynamicProperty} can't specify this one.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Priority {
		int value() default 0;
	}

	/**
	 * Marks a field as collection, with remove/add handling.
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Collection {
		/** If true, An object can be removed from this field according to the configuration. */
		boolean removableField() default false;

		/** If true, An object can be added to this field according to the configuration. */
		boolean addableField() default true;
	}

	/**
	 * Expands a field, i.e. the categories and properties from the field
	 *  is merged to the parent category. <p>
	 * */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Expand {
		/**
		 * If true, categories and properties from this field will be as sub-entry. <p>
		 * As a sub-entry, it can have various config property annotations. <p>
		 * Otherwise, it will be just merged into the parent category.
		 * */
		boolean value() default false;
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

}