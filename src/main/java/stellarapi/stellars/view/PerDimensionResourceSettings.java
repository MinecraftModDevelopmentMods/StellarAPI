package stellarapi.stellars.view;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import stellarapi.api.StellarSkyAPI;
import stellarapi.config.INBTConfig;
import stellarapi.config.SimpleNBTConfig;
import stellarapi.config.property.ConfigProperty;
import stellarapi.config.property.ConfigPropertyString;

public class PerDimensionResourceSettings extends SimpleNBTConfig {
	
	public PerDimensionResourceSettings() {
		for(String resourceId : StellarSkyAPI.getPerDimensionResourceIdList())
			this.addConfigProperty(new ConfigPropertyString(resourceId, resourceId.toLowerCase(),
					"default"));
	}
	
	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Per dimension resource configuration.");
		config.setCategoryRequiresWorldRestart(category, true);
		config.setCategoryLanguageKey(category, "config.category.dimension.perdimresource");
		
		super.setupConfig(config, category);
		
		for(ConfigProperty property : this.listProperties) {
			property.setComment("If this is not set to 'default', \n"
					+ "the resource will be replaced by the resource on the described location.\n"
					+ "Note that resource location is form of '(modid):(actual location)'.");
			property.setLanguageKey("config.property.dimension.perdimresource");
			property.setRequiresWorldRestart(false);
		}
		
		super.setupConfig(config, category);
	}
	
	public ResourceLocation getResourceLocationForId(String id) {
		if(mapProperties.containsKey(id)) {
			ConfigPropertyString property = (ConfigPropertyString) mapProperties.get(id);
			if(!property.getString().equals("default"))
				return new ResourceLocation(property.getString());
		}
		
		return null;
	}

	@Override
	public INBTConfig copy() {
		PerDimensionResourceSettings settings = new PerDimensionResourceSettings();
		super.applyCopy(settings);
		return settings;
	}

}
