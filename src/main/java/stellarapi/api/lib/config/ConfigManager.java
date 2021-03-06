package stellarapi.api.lib.config;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.Configuration;

/**
 * Configuration manager.
 */
public class ConfigManager {

	private Configuration config;
	private List<Pair<String, IConfigHandler>> handlerPairList = Lists.newArrayList();

	public ConfigManager(Configuration config) {
		this.config = config;
	}

	public void register(String category, IConfigHandler cfgHandler) {
		handlerPairList.add(Pair.of(category, cfgHandler));
	}

	public void onSyncConfig(boolean loadFromFile, boolean isLoadPhase) {
		if (loadFromFile)
			config.load();

		for (Pair<String, IConfigHandler> entry : handlerPairList) {
			entry.getValue().setupConfig(config, entry.getKey());
		}

		if (isLoadPhase) {
			for (Pair<String, IConfigHandler> entry : handlerPairList) {
				entry.getValue().loadFromConfig(config, entry.getKey());
			}
		} else {
			for (Pair<String, IConfigHandler> entry : handlerPairList) {
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

}
