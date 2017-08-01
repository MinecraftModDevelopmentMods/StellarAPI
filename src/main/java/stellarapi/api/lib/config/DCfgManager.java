package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;

/**
 * Configuration manager.
 */
public final class DCfgManager {

	public static final String SEPARATOR = ".";

	private static void sync(IDConfigHandler handler, Class<?> cls, Object instance, DConfigNode node) {
		//First pass, checks for the changes and applies them on config-side.
		for(Field field : node.fieldsSet) {
			Object fieldValue = null;
			try {
				fieldValue = field.get(instance);
			} catch (Exception exc) {
				Throwables.propagate(exc); // Can't reach here
			}
			if(fieldValue == null)
				continue;

			DConfigNode.FieldInformation fieldInfo = node.infoMap.get(field);
			ITypeExpansion expansion = DCfgManager.getExpansion(field);

			if(!fieldInfo.isChanged())
				continue;

			if(DCfgManager.isNonSingleton(field)) {
				DynamicConfig.Collection colInfo = field.getAnnotation(DynamicConfig.Collection.class);

				if(colInfo != null && colInfo.isConfigurable() && fieldInfo.isChanged()) {
					for(String key : fieldInfo.keys())
						if(!expansion.hasKey(fieldValue, key)) {
							Object value = null;
							// TODO implement here; how to create this one?
							expansion.setValue(instance, key, value);
						}

					Iterator<String> iterator = expansion.getKeys(fieldValue).iterator();
					while(iterator.hasNext())
						if(!fieldInfo.hasKey(iterator.next()))
							iterator.remove();
				}
			}

			for(String key : fieldInfo.keys()) {
				if(!expansion.hasKey(instance, key))
					continue;

				if(fieldInfo.isLeaf())
					try { // Simulates the singleton case.
						field.set(instance,
								expansion.setValue(fieldValue, key, fieldInfo.getValue(key)));
					} catch (Exception exc) {
						Throwables.propagate(exc); // Can't reach here
					}
			}
		}


		//Second pass, sets up the configuration from the structure of instance.
		for(Field field : node.fieldsSet) {
			Object fieldValue = null;
			try {
				fieldValue = field.get(instance);
			} catch (Exception exc) {
				Throwables.propagate(exc); // Can't reach here
			}

			if(fieldValue == null)
				continue;

			DConfigNode.FieldInformation fieldInfo = node.infoMap.get(field);
			ITypeExpansion expansion = DCfgManager.getExpansion(field);	

			if(DCfgManager.isNonSingleton(field)) {
				DynamicConfig.Collection colInfo = field.getAnnotation(DynamicConfig.Collection.class);

				if(colInfo == null || !colInfo.isConfigurable() || !fieldInfo.isChanged()) {
					for(String key : expansion.getKeys(fieldValue)) {
						Object subValue = expansion.getValue(fieldValue, key);
						if(!fieldInfo.hasKey(key))
							fieldInfo.createAndPut(key, subValue);
					}

					Iterator<String> iterator = fieldInfo.keys().iterator();
					while(!iterator.hasNext())
						if(!expansion.hasKey(fieldValue, iterator.next()))
							iterator.remove();
				}
			}

			for(String key : expansion.getKeys(fieldValue)) {
				if(!fieldInfo.isLeaf()) {
					Object subInstance = expansion.getValue(fieldValue, key);
					DConfigNode subNode = fieldInfo.getInstance(key);
					sync(handler, subInstance.getClass(), subInstance, subNode);
				} else fieldInfo.setValue(key, expansion.getValue(fieldValue, key));
			}
		}
	}

	private static int getPriority(Field field) {
		if(!field.isAnnotationPresent(DynamicConfig.Priority.class))
			return 0;
		return field.getAnnotation(DynamicConfig.Priority.class).value();
	}

	static boolean isNonSingleton(Field field) {
		return false;
	}

	/**
	 * Gives expansion even for singleton fields;
	 * ID is already ""(empty string) in the case.
	 * */
	static ITypeExpansion getExpansion(Field field) {
		return null;
	}

	static boolean isLeaf(Field field) {
		return false;
	}

	static Object evaluateDefault(Field field, @Nullable String subId, DynamicConfig.Default defInfo) {
		return null;
	}

	private static IAnnotationHandler getAnnotationHandler(Annotation annotation) {
		return null;
	}
}
