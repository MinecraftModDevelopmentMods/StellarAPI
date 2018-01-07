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
		config.setCategoryLanguageKey(category, "config.category.worldsettings");
		config.setCategoryRequiresWorldRestart(category, true);

		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasAtmosphere().isFalse)
				return;

			String worldCategory = category + Configuration.CATEGORY_SPLITTER + worldSet.name;
			config.setCategoryComment(worldCategory, "Configure settings for the worldset here.");
			config.setCategoryLanguageKey(worldCategory, "config.category.worldsettings.worldset");
			config.setCategoryRequiresWorldRestart(worldCategory, true);

			Property enabled = config.get(worldCategory, "Enabled", false);
			enabled.setComment("Enable/Disable Stellar API tweak for worlds within this worldset.");
			enabled.setLanguageKey("config.property.worldsettings.enabled");
			enabled.setRequiresWorldRestart(true);

			Property dayLength = config.get(worldCategory, "Day_Length", 24000.0, "", 0.0, Double.MAX_VALUE);
			dayLength.setComment("Tweak length of day (in ticks) with this settings.");
			dayLength.setLanguageKey("config.property.worldsettings.daylength");
			dayLength.setRequiresWorldRestart(true);

			Property monthInDay = config.get(worldCategory, "Month_Length_In_Days", 8.0, "", 0.0, Float.MAX_VALUE);
			monthInDay.setComment("Tweak length of month (in days) with this settings.");
			monthInDay.setLanguageKey("config.property.worldsettings.monthlengthinday");
			monthInDay.setRequiresWorldRestart(true);

			Property dayOffset = config.get(worldCategory, "Day_Offset", 7200.0, "", 0.0, Double.MAX_VALUE);
			dayOffset.setComment("Tweak day offset (in ticks), which determines starting position of celestial objects.");
			dayOffset.setLanguageKey("config.property.worldsettings.dayoffset");
			dayOffset.setRequiresWorldRestart(true);

			Property monthOffset = config.get(worldCategory, "Month_Offset", 4.0, "", 0.0, Float.MAX_VALUE);
			monthOffset.setComment("Tweak month offset (in days), which determines starting phase of the moon.");
			monthOffset.setLanguageKey("config.property.worldsettings.monthoffset");
			monthOffset.setRequiresWorldRestart(true);

			Property minSkyBrightness = config.get(worldCategory, "Minimum_Sky_Brightness", 0.2f, "", 0.0f, 1.0f);
			minSkyBrightness.setComment("Tweak minimum sky brightness, which determines the brightness of night.");
			minSkyBrightness.setLanguageKey("config.property.worldsettings.minskybrightness");
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
					double dayOffset = cfgCat.get("Day_Offset").getDouble();
					double monthOffset = cfgCat.get("Month_Offset").getDouble();
					float minSkyBrightness = (float) cfgCat.get("Minimum_Sky_Brightness").getDouble();

					SAPIReferences.setCelestialPack(worldSet,
							new SAPICelestialPack(day, month, dayOffset, monthOffset, minSkyBrightness));
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
