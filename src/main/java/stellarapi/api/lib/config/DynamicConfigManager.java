package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import net.minecraftforge.common.config.Configuration;

/**
 * Configuration manager.
 */
public class DynamicConfigManager {

	private Configuration config;
	private List<Pair<String, IConfigHandler>> handlerPairList = Lists.newArrayList();

	public static final String SEPARATOR = ".";

	public DynamicConfigManager(Configuration config) {
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

	private static void sync(IDConfig config, Class<?> cls, String catEntry, boolean loading, boolean initial, Object instance) {
		TreeSet<Field> fieldsSet = Sets.newTreeSet(
				new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				if(getPriority(o1) > getPriority(o2))
					return 1;
				else if(getPriority(o1) < getPriority(o2))
					return -1;
				return 0;
			}
		});

		// TODO ordering - how to find changed fields and deal with them first?
		for (Field field : cls.getDeclaredFields()) {
			if (!Modifier.isPublic(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()) != (instance == null))
				continue;
			if (Modifier.isTransient(field.getModifiers()))
				continue;

			
		}

		for(Field field : fieldsSet) {
			IDCfgCategory category = config.getCategory(catEntry);
			String name = field.getName();

			if(config.supportPropType(field.getGenericType())) {
				if(!category.hasProperty(name))
					category.createProperty(name, field.getGenericType());
				IDCfgProperty property = category.getProperty(name);
			} else {
				if(hasFieldExpansion(field)) {
					IFieldExpansion expansion = getFieldExpansion(instance, field);

					if(isExpansionSupported(config, expansion)) {
						//Identify this entry as field

						//Checks for current version of field and compare it with properties
						for(String key : expansion.getKeys()) {
							String propEntry = name + SEPARATOR + key;
							boolean propertyExisted = category.hasProperty(propEntry);

							if(!propertyExisted)
								category.createProperty(propEntry, expansion.getValueType(key));

							IDCfgProperty property = category.getProperty(propEntry);

							if(initial)
								property.setDefaultValue(expansion.getValue(key));

							if(loading) {
								if(!propertyExisted)
									property.setValue(expansion.getValue(key));
								else expansion.setValue(key, property.getValue());
							} else {
								property.setValue(expansion.getValue(key));
							}
						}

						//Checks for config properties and compare it with current field
						for(String propEntry : category.getKeysFor(name)) {
							String key = propEntry.replaceFirst(name + SEPARATOR, "");

							if(loading) {
								Object value = category.getProperty(propEntry).getValue();
								expansion.setValue(key, value);
							} else {
								
							}
						}
					} else {
						//Identify this entry as category
						for(String key : expansion.getKeys()) {
							String subCatEntry = catEntry + SEPARATOR + name + SEPARATOR + key;
						}
					}
				} else {
					// Identify this entry as category
					return;
				}
			}
		}
	}

	private static boolean isExpansionSupported(IDConfig config, IFieldExpansion expansion) {
		if(config.supportPropType(expansion.getValueType()))
			return true;
		for(String key : expansion.getKeys())
			if(!config.supportPropType(expansion.getValueType(key)))
				return false;

		return true;
	}

	private static int getPriority(Field field) {
		if(!field.isAnnotationPresent(DynamicConfig.Priority.class))
			return 0;
		return field.getAnnotation(DynamicConfig.Priority.class).value();
	}

	private static boolean hasFieldExpansion(Field field) {
		return false;
	}

	private static IFieldExpansion getFieldExpansion(Object instance, Field field) {
		return null;
	}

	private static IAnnotationHandler getAnnotationHandler(Annotation annotation) {
		return null;
	}
}
