package stellarapi.api.lib.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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

			if(DCfgManager.hasExpansion(field)) {
				DynamicConfig.Collection colInfo = field.getAnnotation(DynamicConfig.Collection.class);
				ITypeExpansion expansion = DCfgManager.getExpansion(field);				

				if(colInfo != null && colInfo.isConfigurable() && fieldInfo.isChanged()) {
					for(String key : fieldInfo.keys())
						if(!expansion.hasKey(fieldValue, key)) {
							//expansion.setValue(instance, key, value);
						}

					for(String key : expansion.getKeys(fieldValue))
						if(!fieldInfo.hasKey(key))
							expansion.remove(instance, key); // FIXME ConcurrentModificationException
				}
			}

			if(!fieldInfo.isLeaf()) {
				for(String key : fieldInfo.keys()) {
					
				}
			} else {
				
			}
		}


		//Second pass, sets up the configuration from the structure of instance.
		for(Field field : node.fieldsSet) {
			DConfigNode.FieldInformation fieldInfo = node.infoMap.get(field);
			Object fieldValue = null;
			try {
				fieldValue = field.get(instance);
			} catch (Exception exc) {
				Throwables.propagate(exc); // Can't reach here
			}
			if(fieldValue == null)
				continue;

			if(DCfgManager.hasExpansion(field)) {
				DynamicConfig.Collection colInfo = field.getAnnotation(DynamicConfig.Collection.class);
				ITypeExpansion expansion = DCfgManager.getExpansion(field);	

				if(colInfo == null || !colInfo.isConfigurable() || !fieldInfo.isChanged()) {
					for(String key : expansion.getKeys(fieldValue)) {
						Object subValue = expansion.getValue(fieldValue, key);
						if(!fieldInfo.hasKey(key))
							fieldInfo.create(key, subValue);
					}

					for(String key : fieldInfo.keys())
						if(!expansion.hasKey(fieldValue, key))
							fieldInfo.remove(key); // FIXME ConcurrentModificationException
				}
			}
		}
	}

	private static int getPriority(Field field) {
		if(!field.isAnnotationPresent(DynamicConfig.Priority.class))
			return 0;
		return field.getAnnotation(DynamicConfig.Priority.class).value();
	}

	static boolean hasExpansion(Field field) {
		return false;
	}

	static ITypeExpansion getExpansion(Field field) {
		return null;
	}

	static boolean isPrimitive(Type type) {
		return (type instanceof Class) && ((Class)type).isPrimitive();
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
