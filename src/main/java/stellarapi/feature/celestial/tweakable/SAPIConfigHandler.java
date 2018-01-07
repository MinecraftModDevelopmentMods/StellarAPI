package stellarapi.feature.celestial.tweakable;

import com.google.common.collect.Lists;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarapi.api.ICelestialPack;
import stellarapi.api.SAPIReferences;
import stellarapi.api.lib.config.IConfigHandler;
import stellarapi.api.world.worldset.WorldSet;
import stellarapi.impl.celestial.DefaultCelestialPack;

public class SAPIConfigHandler implements IConfigHandler {

	// Yes, bad flag. I lack time to organize these stuffs
	// TODO Fix this flag
	public static boolean forceConfig;

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

			config.setCategoryPropertyOrder(worldCategory,
					Lists.newArrayList(enabled.getName(), dayLength.getName(), monthInDay.getName(),
							dayOffset.getName(), monthOffset.getName(), minSkyBrightness.getName()));
		}
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		forceConfig = config.getCategory(category).get("Force_Config").getBoolean();

		for(WorldSet worldSet : SAPIReferences.getAllWorldSets()) {
			if(worldSet.hasAtmosphere().isFalse)
				return;

			String worldCategory = category + Configuration.CATEGORY_SPLITTER + worldSet.name;

			ICelestialPack pack = SAPIReferences.getCelestialPack(worldSet);
			if(pack == null || "Stellar API".equals(pack.getPackName())) {
				ConfigCategory cfgCat = config.getCategory(worldCategory);
				if(cfgCat.get("Enabled").getBoolean()) {
					double day = cfgCat.get("Day_Length").getDouble();
					double month = cfgCat.get("Month_Length_In_Days").getDouble();
					double dayOffset = cfgCat.get("Day_Offset").getDouble();
					double monthOffset = cfgCat.get("Month_Offset").getDouble();
					float minSkyBrightness = (float) cfgCat.get("Minimum_Sky_Brightness").getDouble();

					SAPIReferences.setCelestialPack(worldSet,
							new SAPICelestialPack(day, month, dayOffset, monthOffset, minSkyBrightness));
				} else if(pack == null && worldSet == SAPIReferences.exactOverworld()) {
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
