package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;
import stellarapi.api.lib.config.DynamicConfig.Default;

/**
 * Configuration manager.
 */
public final class DCfgManager {

	private Configuration config;
	private List<Pair<String, IConfigHandler>> handlerPairList = Lists.newArrayList();

	public static final String SEPARATOR = ".";

	public DCfgManager(Configuration config) {
		this.config = config;
	}

	public void register(String category, IConfigHandler cfgHandler) {
		handlerPairList.add(Pair.of(category, cfgHandler));
	}

	public void onSyncConfig(boolean loadFromFile, boolean isLoadPhase) {
		if (loadFromFile)
			config.load();

		for (Pair<String, IConfigHandler> entry : this.handlerPairList) {
			entry.getValue().setupConfig(config, entry.getKey());
		}

		if (isLoadPhase) {
			for (Pair<String, IConfigHandler> entry : this.handlerPairList) {
				entry.getValue().loadFromConfig(config, entry.getKey());
			}
		} else {
			for (Pair<String, IConfigHandler> entry : this.handlerPairList) {
				entry.getValue().saveToConfig(config, entry.getKey());
			}
		}

		if (config.hasChanged())
			config.save();
	}

	/**
	 * load the configuration values from the configuration file
	 */
	public void syncFromFile() {
		onSyncConfig(true, true);
	}

	/**
	 * save the GUI-altered values to disk
	 */
	public void syncFromGUI() {
		onSyncConfig(false, true);
	}

	/**
	 * save the variables (fields) to disk
	 */
	public void syncFromFields() {
		onSyncConfig(false, false);
	}

	public Configuration getConfig() {
		return this.config;
	}

	// TODO Overhaul this system; Configuration Themes
	// (Old) Handle these properly(config-dependents):
	//  Attribute attribute; - config-specific singular property or singular category. No expansion(@Default if prop)
	//  Speed speed; - config-specific singular property or singular category. Has expansion for Default(@Default or expansion)
	//  Vector3 vector; - config-specific singular property or compound property
	private static void sync(IDConfig config, Class<?> cls, String catEntry, boolean loading, boolean initial, DCfgInstance wrapper) {
		//First pass, setting up. (sets default on the initial case)
		for(Field field : wrapper.fieldsSet) {
			DCfgInstance.FieldInformation information = wrapper.infoMap.get(field);
			information.isProperty = isProperty(config, information.type);
			information.checked = false;

			if(initial) {
				
			}
		}

		//Second pass, checks for (config-side) changed fields first. Looping the whole set due to in-situ changes
		for(Field field : wrapper.fieldsSet) {
			DCfgInstance.FieldInformation information = wrapper.infoMap.get(field);
			if(isChanged(config, catEntry, field.getName(), cls, field, initial, true)) {
				Object value;
				try {
					value = field.get(wrapper.instance);
				} catch (Exception exception) {
					Throwables.propagate(exception);
				}

				information.checked = true;
			}
		}

		//Third pass, checks for unchanged fields.
		for(Field field : wrapper.fieldsSet) {
			DCfgInstance.FieldInformation information = wrapper.infoMap.get(field);
			if(!information.checked && !isNew(config, catEntry, field.getName(), cls, field, initial)) {
				information.checked = true;
			}
		}

		//Fourth pass, checks for created fields.
		for(Field field : wrapper.fieldsSet) {
			DCfgInstance.FieldInformation information = wrapper.infoMap.get(field);
			if(!information.checked) {
				
			}
		}
	}

	private static void sync(IDConfig config, String catEntry, String thisEntry, Type type, Object value, boolean isProperty, boolean loading) {
		IDCfgCategory category = config.getCategory(catEntry);

		if(config.supportPropType(type)) {
			//if(!category.hasProperty(name))
			//	category.createProperty(name, field.getGenericType());
			//IDCfgProperty property = category.getProperty(name);
		} else {
			if(hasExpansion(type)) {
				ITypeExpansion expansion = getExpansion(type);

				//Checks for current version of field and compare it with configuration.
				for(String key : expansion.getKeys(value)) {
					String subEntry = thisEntry + SEPARATOR + key;

				}

				//Checks for configuration and compare it with current field
				if(isProperty) {
					for(String subEntry : category.getKeysFor(thisEntry)) {
						
					}
				} else {
					
				}
			} else {
				// Identify this entry as category
				return;
			}
		}
	}

	/**
	 * Checks if certain type is property under certain config system.
	 * */
	private static boolean isProperty(IDConfig config, Type type) {
		if(config.supportPropType(type))
			return true;
		if(!hasExpansion(type))
			return false;

		ITypeExpansion expansion = getExpansion(type);
		for(Type subType : expansion.getValueTypes())
			if(!isProperty(config, subType))
				return false;

		return true;
	}

	/**
	 * @param catEntry the category entry which contains the field with which this property/category is related
	 * @param thisEntry the entry under the catEntry
	 * */
	private static boolean isChanged(IDConfig config, String catEntry, String thisEntry, Type type, Object value, boolean isProperty, boolean isRoot) {
		//If this (sub)field is a singular property
		if(isProperty && config.supportPropType(type)) {
			if(config.hasCategory(catEntry) && config.getCategory(catEntry).hasProperty(thisEntry))
				return config.getCategory(catEntry).getProperty(thisEntry).isChanged();
			else return isRoot? false : true;
		}

		//If this (sub)field is a singular category
		if(!hasExpansion(type)) {
			String entry = catEntry + SEPARATOR + thisEntry;
			if(config.hasCategory(entry))
				return config.getCategory(entry).isChanged();
			else return isRoot? false : true;
		}

		//If this (sub)field is a compound
		ITypeExpansion expansion = getExpansion(type);
		for(String key : expansion.getKeys(value)) {
			Object subValue = expansion.getValue(value, key);

			if(isChanged(config, catEntry, thisEntry + SEPARATOR + key, type, value, isProperty, false))
				return true;
		}

		return false;
	}

	private static boolean isNew(IDConfig config, String catEntry, String thisEntry, Type type, Object value, boolean isProperty) {
		//If this (sub)field is a singular property
		if(isProperty && config.supportPropType(type))
			return !(config.hasCategory(catEntry) && config.getCategory(catEntry).hasProperty(thisEntry));

		//If this (sub)field is a singular category
		if(!hasExpansion(type)) {
			String entry = catEntry + SEPARATOR + thisEntry;
			return !config.hasCategory(entry);
		}

		return false;
	}

	private static int getPriority(Field field) {
		if(!field.isAnnotationPresent(DynamicConfig.Priority.class))
			return 0;
		return field.getAnnotation(DynamicConfig.Priority.class).value();
	}

	static boolean hasExpansion(Type type) {
		return false;
	}

	static ITypeExpansion getExpansion(Type type) {
		return null;
	}

	static boolean isPrimitive(Type type) {
		return (type instanceof Class) && ((Class)type).isPrimitive();
	}

	static boolean hasDefault(Type type, DynamicConfig.Default[] defaults) {
		return false;
	}

	static Object evaluateDefault(Type type, Default[] defaults) {
		return null;
	}

	private static IAnnotationHandler getAnnotationHandler(Annotation annotation) {
		return null;
	}
}
