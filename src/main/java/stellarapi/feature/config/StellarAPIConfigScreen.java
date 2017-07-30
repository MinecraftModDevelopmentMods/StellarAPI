package stellarapi.feature.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import stellarapi.StellarAPI;

public class StellarAPIConfigScreen extends GuiConfig {

	public StellarAPIConfigScreen(GuiScreen parentScreen) {
		super(parentScreen, getConfigElement(), StellarAPI.MODID, false, false, "Stellar API");
	}

	private static List<IConfigElement> getConfigElement() {
		Configuration config = StellarAPI.INSTANCE.getCfgManager().getConfig();

		List<IConfigElement> retList = Lists.newArrayList();
		for (String category : config.getCategoryNames())
			if (!category.contains(Configuration.CATEGORY_SPLITTER))
				retList.add(new ConfigElement(config.getCategory(category)));
		return retList;
	}

}
