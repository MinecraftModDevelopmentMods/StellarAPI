package stellarapi.api.lib.config;

import java.lang.reflect.Type;

public interface IDCfgCategory {

	Iterable<String> getKeysFor(String name);

	boolean hasProperty(String propEntry);

	void createProperty(String propEntry, Type type);

	IDCfgProperty getProperty(String propEntry);

	boolean isChanged();

}