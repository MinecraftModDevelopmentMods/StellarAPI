package stellarapi.api.lib.config.property;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class ConfigPropertyList extends ConfigProperty {

	public ConfigPropertyList(String configKey, String dataKey) {
		super(configKey, dataKey);
	}

	public void setupConfiguration(Configuration config, String category) {
		this.property = config.get(category, this.configKey, this.getDefaultValues(), null, this.getType());
	}

	public void setIsListLengthFixed(boolean isListLengthFixed) {
		property.setIsListLengthFixed(isListLengthFixed);
	}

	public void setMaxListLength(int max) {
		property.setMaxListLength(max);
		this.resizeValuesToLength(property.isListLengthFixed(), property.getMaxListLength());
	}

	protected abstract Property.Type getType();

	protected abstract String[] getDefaultValues();

	protected abstract void resizeValuesToLength(boolean fixed, int length);
}
