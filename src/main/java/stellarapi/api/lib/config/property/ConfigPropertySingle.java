package stellarapi.api.lib.config.property;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class ConfigPropertySingle extends ConfigProperty {
	
	public ConfigPropertySingle(String configKey, String dataKey) {
		super(configKey, dataKey);
	}
	
	public void setupConfiguration(Configuration config, String category) {
		this.property = config.get(category, this.configKey, this.getDefaultValue(), null, this.getType());
	}
	
	protected abstract Property.Type getType();
	protected abstract String getDefaultValue();
	
	public void setValidValues(String[] validValues) {
		property.setValidValues(validValues);
	}
}
