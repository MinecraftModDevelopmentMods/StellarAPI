package stellarapi.feature.celestial.tweakable;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.SAPIReferences;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.impl.celestial.DefaultCelestialPack;

public class SAPIConfigHandler implements IConfigHandler {

	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configure world settings for each worldsets.");
		config.setCategoryLanguageKey(category, "config.category.worldsettings"); // TODO Localize
		config.setCategoryRequiresWorldRestart(category, true);

		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasAtmosphere().isFalse)
				return;

			String worldCategory = category + Configuration.CATEGORY_SPLITTER + worldSet.name;
			config.setCategoryComment(worldCategory, "Configure settings for the worldset here.");
			config.setCategoryLanguageKey(worldCategory, "config.category.worldsettings.worldset");
			config.setCategoryRequiresWorldRestart(worldCategory, true);

			Property enabled = config.get(worldCategory, "Enabled", false);
			enabled.setComment("Enable Stellar API tweak for worlds within this worldset.");
			enabled.setLanguageKey("config.property.worldsettings.enabled");
			enabled.setRequiresWorldRestart(true);

			Property dayLength = config.get(worldCategory, "Day_Length", 24000.0, "", 0.0, Double.MAX_VALUE);
			dayLength.setComment("Tweak length of day with this settings.");
			dayLength.setLanguageKey("config.property.worldsettings.daylength");
			dayLength.setRequiresWorldRestart(true);

			Property monthInDay = config.get(worldCategory, "Month_Length_In_Days", 8.0, "", 0.0, Float.MAX_VALUE);
			monthInDay.setComment("Tweak length of month (in days) with this settings.");
			monthInDay.setLanguageKey("config.property.worldsettings.monthlengthinday");
			monthInDay.setRequiresWorldRestart(true);
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasAtmosphere().isFalse)
				return;

			String worldCategory = category + Configuration.CATEGORY_SPLITTER + worldSet.name;

			if(SAPIReferences.getCelestialPack(worldSet) == null) {
				ConfigCategory cfgCat = config.getCategory(worldCategory);
				if(cfgCat.get("Enabled").getBoolean()) {
					double day = cfgCat.get("Day_Length").getDouble();
					double month = cfgCat.get("Month_Length_In_Days").getDouble();

					SAPIReferences.setCelestialPack(worldSet, new SAPICelestialPack(day, month));
				} else if(worldSet == SAPIReferences.exactOverworld()) {
					SAPIReferences.setCelestialPack(worldSet, new DefaultCelestialPack());
				}
			}
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		// No field change
	}
}
