package stellarapi.proxy;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import stellarapi.SAPIClientEventHook;
import stellarapi.StellarAPI;
import stellarapi.api.gui.overlay.OverlayRegistry;
import stellarapi.api.lib.config.DCfgManager;
import stellarapi.feature.gui.overlay.OverlayHandler;
import stellarapi.feature.gui.overlay.OverlaySetMain;
import stellarapi.feature.gui.overlay.OverlaySetStellarAPI;
import stellarapi.feature.gui.overlay.configurator.OverlayConfiguratorType;
import stellarapi.feature.gui.overlay.time.OverlayTimeType;

public class ClientProxy extends CommonProxy implements IProxy {

	private OverlayHandler overlay;
	private DCfgManager guiConfig;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		this.overlay = new OverlayHandler();

		MinecraftForge.EVENT_BUS.register(new SAPIClientEventHook(this.overlay));
		MinecraftForge.EVENT_BUS.register(new StellarAPIClientFMLEventHook(this.overlay));

		this.guiConfig = new DCfgManager(
				StellarAPI.getConfiguration(event.getModConfigurationDirectory(), "GuiConfig.cfg"));

		OverlayRegistry.registerOverlaySet("main", new OverlaySetMain());
		OverlayRegistry.registerOverlaySet("stellarapi", new OverlaySetStellarAPI());

		OverlayRegistry.registerOverlay("position", new OverlayConfiguratorType(), guiConfig);
		OverlayRegistry.registerOverlay("stellarapi.time", new OverlayTimeType(), guiConfig);

		ModelLoader.setCustomModelResourceLocation(StellarAPI.instance.telescope, 0,
				new ModelResourceLocation("stellarapi:deftelescope", "inventory"));
		ModelLoader.setCustomModelResourceLocation(StellarAPI.instance.filteredTelescope, 0,
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
	public void registerClientTask(Runnable runnable) {
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public Entity getRenderViewEntity() {
		Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
		return renderViewEntity != null? renderViewEntity : this.getClientPlayer();
	}
}
