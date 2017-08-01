package stellarapi.api.lib.config;

import java.lang.reflect.Field;

/**
 * Abstract interface for dynamic configuration. <p>
 * A configuration is defined as a mapping from String-type entry
 *  to {@link IDCfgCategory Configuration Category}.<p>
 * Can be synced and formatted.
 * */
public interface IDConfigHandler {

	/**
	 * Check if this config supports the handling.
	 * */
	public boolean supportHandling(Field field, String entry);

}