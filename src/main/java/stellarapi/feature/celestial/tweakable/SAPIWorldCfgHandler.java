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

public class SAPIWorldCfgHandler implements IConfigHandler {

	private final WorldSet worldSet;

	private boolean enabled = false;

	boolean sunExist = true, moonExist = true;
	double dayLength = 24000.0;
	double dayOffset = 7200.0;

	double monthInDay = 8.0;
	double monthOffset = 4.0;

	float minimumSkyBrightness = 0.2f;

	public SAPIWorldCfgHandler(WorldSet worldSet) {
		this.worldSet = worldSet;
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		config.setCategoryComment(category, "Configure settings for the worldset here.");
		config.setCategoryLanguageKey(category, "config.category.worldsettings.worldset");
		config.setCategoryRequiresWorldRestart(category, true);

		Property enabled = config.get(category, "Enabled", false);
		enabled.setComment("Enable/Disable Stellar API tweak for worlds within this worldset.");
		enabled.setLanguageKey("config.property.worldsettings.enabled");
		enabled.setRequiresWorldRestart(true);

		Property hasSun = config.get(category, "Sun_Exist", true);
		hasSun.setComment("Configure existence of the sun. This affects the brightness as well.");
		hasSun.setLanguageKey("config.property.worldsettings.sunexist");
		hasSun.setRequiresWorldRestart(true);

		Property hasMoon = config.get(category, "Moon_Exist", true);
		hasMoon.setComment("Configure existence of the moon.");
		hasMoon.setLanguageKey("config.property.worldsettings.moonexist");
		hasMoon.setRequiresWorldRestart(true);

		Property dayLength = config.get(category, "Day_Length", 24000.0, "", 0.0, Double.MAX_VALUE);
		dayLength.setComment("Tweak length of day (in ticks) with this settings.");
		dayLength.setLanguageKey("config.property.worldsettings.daylength");
		dayLength.setRequiresWorldRestart(true);

		Property monthInDay = config.get(category, "Month_Length_In_Days", 8.0, "", 0.0, Float.MAX_VALUE);
		monthInDay.setComment("Tweak length of month (in days) with this settings.");
		monthInDay.setLanguageKey("config.property.worldsettings.monthlengthinday");
		monthInDay.setRequiresWorldRestart(true);

		Property dayOffset = config.get(category, "Day_Offset", 7200.0, "", 0.0, Double.MAX_VALUE);
		dayOffset.setComment("Tweak day offset (in ticks), which determines starting position of celestial objects.");
		dayOffset.setLanguageKey("config.property.worldsettings.dayoffset");
		dayOffset.setRequiresWorldRestart(true);

		Property monthOffset = config.get(category, "Month_Offset", 4.0, "", 0.0, Float.MAX_VALUE);
		monthOffset.setComment("Tweak month offset (in days), which determines starting phase of the moon.");
		monthOffset.setLanguageKey("config.property.worldsettings.monthoffset");
		monthOffset.setRequiresWorldRestart(true);

		Property minSkyBrightness = config.get(category, "Minimum_Sky_Brightness", 0.2f, "", 0.0f, 1.0f);
		minSkyBrightness.setComment("Tweak minimum sky brightness, which determines the brightness of night.");
		minSkyBrightness.setLanguageKey("config.property.worldsettings.minskybrightness");

		config.setCategoryPropertyOrder(category,
				Lists.newArrayList(enabled.getName(),
						hasSun.getName(), hasMoon.getName(),
						dayLength.getName(), monthInDay.getName(),
						dayOffset.getName(), monthOffset.getName(), minSkyBrightness.getName()));
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		ConfigCategory cfgCat = config.getCategory(category);
		this.enabled = cfgCat.get("Enabled").getBoolean();
		this.sunExist = cfgCat.get("Sun_Exist").getBoolean();
		this.moonExist = cfgCat.get("Moon_Exist").getBoolean();
		this.dayLength = cfgCat.get("Day_Length").getDouble();
		this.monthInDay = cfgCat.get("Month_Length_In_Days").getDouble();
		this.dayOffset = cfgCat.get("Day_Offset").getDouble();
		this.monthOffset = cfgCat.get("Month_Offset").getDouble();
		this.minimumSkyBrightness = (float) cfgCat.get("Minimum_Sky_Brightness").getDouble();

		ICelestialPack pack = SAPIReferences.getCelestialPack(worldSet);
		if(pack == null) {
			if(this.enabled)
				SAPIReferences.setCelestialPack(this.worldSet, SAPICelestialPack.INSTANCE);
			else if(worldSet == SAPIReferences.exactOverworld())
				SAPIReferences.setCelestialPack(this.worldSet, DefaultCelestialPack.INSTANCE);
		}
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		// Do nothing here
	}

}
