package stellarapi.feature.config;

import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.ICelestialPack;
import stellarapi.api.SAPIReferences;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.feature.celestial.tweakable.SAPIWorldCfgHandler;
import stellarapi.impl.celestial.DefaultCelestialPack;

public class SAPIConfigHandler implements IConfigHandler {
	private boolean forceConfig;
	private Map<WorldSet, SAPIWorldCfgHandler> subHandlers = Maps.newIdentityHashMap();

	public boolean forceConfig() {
		return this.forceConfig;
	}

	public SAPIWorldCfgHandler getHandler(WorldSet worldSet) {
		return subHandlers.get(worldSet);
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configure world settings for each worldsets.");
		config.setCategoryLanguageKey(category, "config.category.worldsettings");
		config.setCategoryRequiresWorldRestart(category, true);

		Property forceChange = config.get(category, "Force_Config", false);
		forceChange.setComment("Set this to true to force configuration change to the existing world"
				+ " which is opened at least once with Stellar API.");
		forceChange.setLanguageKey("config.property.worldsettings.forceconfig");
		forceChange.setRequiresWorldRestart(true);

		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasSky().isFalse || worldSet == SAPIReferences.endType())
				return;
			// TODO Remove specialty of End Type and implement things right to be applied on The End as well
			// Don't let packs give the raw worldprovider.

			SAPIWorldCfgHandler handler = subHandlers.computeIfAbsent(worldSet, k -> new SAPIWorldCfgHandler(k));
			handler.setupConfig(config, category + Configuration.CATEGORY_SPLITTER + worldSet.name);
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		forceConfig = config.getCategory(category).get("Force_Config").getBoolean();

		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasSky().isFalse || worldSet == SAPIReferences.endType())
				return;

			SAPIWorldCfgHandler handler = subHandlers.get(worldSet);
			handler.loadFromConfig(config, category + Configuration.CATEGORY_SPLITTER + worldSet.name);
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		// No field change
	}
}
