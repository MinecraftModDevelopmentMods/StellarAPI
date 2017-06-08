package stellarapi.api.lib.config;

import java.lang.reflect.Type;

/**
 * Abstract interface for dynamic configuration. <p>
 * A configuration is defined as a mapping from String-type entry
 *  to {@link IDCfgCategory Configuration Category}.<p>
 * Can be synced and formatted.
 * */
public interface IDConfig {

	public boolean hasCategory(String entry);
	public IDCfgCategory getCategory(String entry);

	/**
	 * Checks if this config supports certain field type or not.
	 * */
	public boolean supportPropType(Type type);

}