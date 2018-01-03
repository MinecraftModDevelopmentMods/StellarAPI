package stellarapi;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.api.gui.overlay.OverlayRegistry;
import stellarapi.feature.gui.overlay.OverlayHandler;
import stellarapi.feature.gui.overlay.OverlaySetMain;
import stellarapi.feature.gui.overlay.OverlaySetStellarAPI;
import stellarapi.feature.gui.overlay.configurator.OverlayConfiguratorType;
import stellarapi.feature.gui.overlay.time.OverlayTimeType;
import worldsets.api.lib.config.ConfigManager;

public class ClientProxy extends CommonProxy implements IProxy {

	private OverlayHandler overlay;
	private ConfigManager guiConfig;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		this.overlay = new OverlayHandler();

		MinecraftForge.EVENT_BUS.register(new SAPIClientForgeEventHook(this.overlay));

		this.guiConfig = new ConfigManager(
				StellarAPI.getConfiguration(event.getModConfigurationDirectory(), "GuiConfig.cfg"));

		OverlayRegistry.registerOverlaySet("main", new OverlaySetMain());
		OverlayRegistry.registerOverlaySet("stellarapi", new OverlaySetStellarAPI());
		OverlayRegistry.registerOverlay("position", new OverlayConfiguratorType(), guiConfig);
		OverlayRegistry.registerOverlay("stellarapi.time", new OverlayTimeType(), guiConfig);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(SAPIItems.INSTANCE.telescope, 0,
				new ModelResourceLocation("stellarapi:deftelescope", "inventory"));
		ModelLoader.setCustomModelResourceLocation(SAPIItems.INSTANCE.filteredTelescope, 0,
				new ModelResourceLocation("stellarapi:deffilteredtelescope", "inventory"));
	}

	@Override
	public void load(FMLInitializationEvent event) throws IOException {
		super.load(event);
		guiConfig.syncFromFile();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		overlay.initialize(Minecraft.getMinecraft());
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public void registerTask(Runnable runnable) {
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}

}
