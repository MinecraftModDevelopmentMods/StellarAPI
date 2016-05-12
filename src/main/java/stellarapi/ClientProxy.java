package stellarapi;

import java.io.IOException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import stellarapi.api.gui.loading.ICombinedProgressUpdate;
import stellarapi.api.gui.overlay.OverlayRegistry;
import stellarapi.api.lib.config.ConfigManager;
import stellarapi.feature.gui.loading.CombinedLoadingScreenRenderer;
import stellarapi.feature.gui.overlay.OverlayHandler;
import stellarapi.feature.gui.overlay.OverlaySetMain;
import stellarapi.feature.gui.overlay.OverlaySetStellarAPI;
import stellarapi.feature.gui.overlay.configurator.OverlayConfiguratorType;
import stellarapi.feature.gui.overlay.time.OverlayTimeType;

public class ClientProxy extends CommonProxy implements IProxy {
		
	private OverlayHandler overlay;
	private ConfigManager guiConfig;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		this.overlay = new OverlayHandler();
		
		MinecraftForge.EVENT_BUS.register(new StellarAPIClientForgeEventHook(this.overlay));
		FMLCommonHandler.instance().bus().register(new StellarAPIClientFMLEventHook(this.overlay));
		
		this.guiConfig = new ConfigManager(StellarAPI.getConfiguration(
				event.getModConfigurationDirectory(), "GuiConfig.cfg"));
		
		OverlayRegistry.registerOverlaySet("main", new OverlaySetMain());
		OverlayRegistry.registerOverlaySet("stellarapi", new OverlaySetStellarAPI());
		OverlayRegistry.registerOverlay("position", new OverlayConfiguratorType(), guiConfig);
		OverlayRegistry.registerOverlay("time", new OverlayTimeType(), guiConfig);
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
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public ICombinedProgressUpdate getLoadingProgress() {
		Minecraft mc = Minecraft.getMinecraft();
		if(!(mc.loadingScreen instanceof CombinedLoadingScreenRenderer))
			mc.loadingScreen = new CombinedLoadingScreenRenderer(mc);
		return (CombinedLoadingScreenRenderer) mc.loadingScreen;
	}
}
