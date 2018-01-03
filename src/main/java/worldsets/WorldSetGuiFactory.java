package worldsets;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import worldsets.api.WAPIReferences;

public class WorldSetGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) { }

	@Override
	public boolean hasConfigGui() { return true; }

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		Configuration config = WorldSetAPI.INSTANCE.getConfigManager().getConfig();
		List<IConfigElement> list = Lists.newArrayList();
		for (String category : config.getCategoryNames())
			if (!category.contains(Configuration.CATEGORY_SPLITTER))
				list.add(new ConfigElement(config.getCategory(category)));
		return new GuiConfig(parentScreen, list, WAPIReferences.MODID, true, true, "WorldSets");
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }

}
